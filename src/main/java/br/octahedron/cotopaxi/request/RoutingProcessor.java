/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.cotopaxi.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.octahedron.cotopaxi.HTTPMethod;
import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.InstanceHandler;

/**
 * This entity is responsible to finds the controller for a given request.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RoutingProcessor implements RequestProcessor {

	/*
	 * ** Mapping Workflow
	 * 
	 * First, it creates a key for the url/method pair. Then, it looks for a mapping at the
	 * cachedURLs map. If nothing is found at cache, check for the key at the static map. If it
	 * fails one more time, look for it the dynamicURLs map.
	 * 
	 * This dynamicURLs map consists of a regex/ModelMapping map. The search on this map is
	 * iterative, and at worse case it can take O(n) attempts. On each attempt, the url is checked
	 * using the regex and, if matches, return the mapped ModelMaping.
	 * 
	 * If no ModelMapping is found for the given url/method pair, an NotFoundException is
	 * thrown.
	 */

	public static final String NOT_FOUND_URL = "notFoundUrl";
	public static final String NOT_FOUND_METHOD = "notFoundMethod";

	private static final String METHOD_SEPARATOR = "%%";
	private static final String SEPARATOR = "/";
	/* pattern for non static urls */
	private static final Pattern nonStaticPattern = Pattern.compile("^((/[a-zA-Z_0-9]+)*(/\\{[a-zA-Z_0-9]+\\})+(/[a-zA-Z_0-9]+)*)+/?$");
	/* pattern for variables */
	private static final Pattern variablePattern = Pattern.compile("\\{[a-zA-Z_0-9]+\\}");

	// Cached URLS vs Controller Class
	private Map<String, Class<? extends Controller>> cachedURLs = new HashMap<String, Class<? extends Controller>>();
	// static URLS vs Controller Class
	private Map<String, Class<? extends Controller>> staticURLs = new HashMap<String, Class<? extends Controller>>();
	// dynamic URLS vs Controller Class
	private Map<String, Class<? extends Controller>> dynamicURLs = new HashMap<String, Class<? extends Controller>>();
	// reverse Map: Controller Class vs. Mapping URL
	private Map<Class<? extends Controller>, String> urlRoute = new HashMap<Class<? extends Controller>, String>();

	/**
	 * Adds a new {@link Controller} route
	 */
	protected void addRoute(String url, HTTPMethod method, Class<? extends Controller> controller) {
		this.urlRoute.put(controller, url);
		if (this.isStaticURL(url)) {
			this.staticURLs.put(this.getURLKey(url, method), controller);
		} else {
			// replace all the {var} to create a url regex and adds it to the dynamicUrlsMap
			String urlRegex = this.generateRegex(url);
			this.dynamicURLs.put(this.getURLKey(urlRegex, method), controller);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.controller.RequestProcessor#process(br.octahedron.cotopaxi.controller
	 * .Request)
	 */
	@Override
	public void process(Request request) throws RequestProcessingException {
		Class<? extends Controller> controller = this.findController(request.getURL(), request.getMethod());
		
		String routeURL = this.urlRoute.get(controller);
		if (!this.isStaticURL(routeURL)) {
			this.extractURLParameters(routeURL, request);
		}
		
		try {
			request.setController(InstanceHandler.getInstance(controller));
		} catch (InstantiationException ex) {
			throw new RequestProcessingException(controller.getCanonicalName(), ex);
		}
	}

	/**
	 * Maps the given URL/Method to a {@link Controller} class
	 * @param url the URL
	 * @param method the {@link HTTPMethod}.
	 * @return The {@link Controller} class to handler the request
	 * @throws PageNotFoundExeption
	 *             If no {@link MetadataHandler} is found for the given URL/{@link HTTPMethod} pair
	 */
	protected Class<? extends Controller> findController(String url, HTTPMethod method) throws NotFoundExeption {
		Class<? extends Controller> result;

		String key = this.getURLKey(url, method);
		if (this.cachedURLs.containsKey(key)) {
			result = this.cachedURLs.get(key);
		} else if (this.staticURLs.containsKey(key)) {
			result = this.staticURLs.get(key);
		} else {
			result = this.findDynamicURL(key);
		}

		// cache result and return
		if (result != null) {
			this.cachedURLs.put(key, result);
			return result;
		} else {
			throw new NotFoundExeption(url, method);
		}
	}

	/**
	 * Extracts the dynamic request parameters on the URL. It checks the mapping URL pattern and the
	 * accessed URL and maps the parameters, setting it on the {@link RequestWrapper}.
	 */
	private void extractURLParameters(String routeURL, Request request) {
		String[] mappingURLTokens = routeURL.split(SEPARATOR);
		String[] requestURLTokens = request.getURL().split(SEPARATOR);
		for (int i = 1; i < mappingURLTokens.length; i++) {
			Matcher m = variablePattern.matcher(mappingURLTokens[i]);
			if (m.matches()) {
				String attName = mappingURLTokens[i];
				attName = attName.substring(1, attName.length() - 1);
				String value = requestURLTokens[i];
				request.setRequestParameter(attName, value);
			}
		}
	}

	/**
	 * Finds the Controller for a given URL, if this URL represents a dynamic URL.
	 * 
	 * @param key
	 *            the URL key, generated using the getURLKey
	 * 
	 * @return The {@link Controller} class for the given URL, if exists, or <code>null</code>
	 *         otherwise.
	 */
	private Class<? extends Controller> findDynamicURL(String key) {
		Class<? extends Controller> result = null;
		for (Entry<String, Class<? extends Controller>> entry : this.dynamicURLs.entrySet()) {
			if (key.matches(entry.getKey())) {
				result = entry.getValue();
				break;
			}
		}
		return result;
	}

	/**
	 * Gets the key for a given URL and method. This key is used by the maps.
	 */
	private String getURLKey(String url, HTTPMethod method) {
		url = url.toLowerCase();
		if (url.length() > 1 && url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url + METHOD_SEPARATOR + method.name();
	}

	/**
	 * Generates a regex to recognize the given dynamic URL.
	 * 
	 * @param url
	 *            the dynamicUrl to be transformed to a regex.
	 * @return a regex for the given URL.
	 */
	private String generateRegex(String url) {
		return variablePattern.matcher(url).replaceAll(Matcher.quoteReplacement("[a-zA-Z_0-9]+"));
	}

	/**
	 * Checks if the given Mapping URL is a static URL. This not check if a requested URL is static
	 * or not, only URLs from Mappings.
	 * 
	 * @param url
	 *            the URL to be checked.
	 * @return <code>true</code> if the URL is static, <code>false</code> otherwise
	 */
	private boolean isStaticURL(String url) {
		return !nonStaticPattern.matcher(url).matches();
	}
}
