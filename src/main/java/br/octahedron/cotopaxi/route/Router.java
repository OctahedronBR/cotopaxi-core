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
package br.octahedron.cotopaxi.route;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.controller.ControllerDescriptor;
import br.octahedron.util.Log;


/**
 * This entity is responsible to finds the controller for a given request.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class Router {

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
	 * If no ModelMapping is found for the given url/method pair, an NotFoundException is thrown.
	 */

	public static final String NOT_FOUND_URL = "notFoundUrl";
	public static final String NOT_FOUND_METHOD = "notFoundMethod";

	private static final String METHOD_SEPARATOR = "%%";
	private static final String SEPARATOR = "/";
	// Log
	private static final Log log = new Log(Router.class);
	/* pattern for non static urls */
	private static final Pattern nonStaticPattern = Pattern.compile("^((/[a-zA-Z_0-9]+)*(/\\{[a-zA-Z_0-9]+\\})+(/[a-zA-Z_0-9]+)*)+/?$");
	/* pattern for variables */
	private static final Pattern variablePattern = Pattern.compile("\\{[a-zA-Z_0-9]+\\}");

	// Cached URLS vs ControllerDescriptor
	private Map<String, ControllerDescriptor> cachedURLs = new HashMap<String, ControllerDescriptor>();
	// static URLS vs ControllerDescriptor
	private Map<String, ControllerDescriptor> staticURLs = new HashMap<String, ControllerDescriptor>();
	// dynamic URLS vs ControllerDescriptor
	private Map<String, ControllerDescriptor> dynamicURLs = new HashMap<String, ControllerDescriptor>();
	// reverse Map: ControllerDescriptor vs. Mapping URL
	private Map<ControllerDescriptor, String> urlRoute = new HashMap<ControllerDescriptor, String>();

	/**
	 * Adds a new route
	 */
	public void addRoute(ControllerDescriptor controllerDesc) {
		String url = controllerDesc.getUrl();
		String method = controllerDesc.getHttpMethod();
		log.info("Adding route for %s %s", method, url);
		this.urlRoute.put(controllerDesc, url);
		if (this.isStaticURL(url)) {
			this.staticURLs.put(this.getURLKey(url, method), controllerDesc);
		} else {
			// replace all the {var} to create a url regex and adds it to the dynamicUrlsMap
			String urlRegex = this.generateRegex(url);
			this.dynamicURLs.put(this.getURLKey(urlRegex, method), controllerDesc);
		}
	}

	/**
	 * Routes a request to a controller.
	 * 
	 * @return The {@link ControllerDescriptor} that describes the controller for the given request.
	 * @throws NotFoundExeption
	 *             If there's controller for the given url
	 */
	public ControllerDescriptor route(HttpServletRequest request) throws NotFoundExeption {
		String url = request.getRequestURI().toLowerCase();
		String method = request.getMethod().toLowerCase();
		ControllerDescriptor result = this.findController(url, method);
		log.debug("Controller for %s %s founded: %s", method, url, result.getControllerName());
		
		String routeURL = this.urlRoute.get(result);
		if (!this.isStaticURL(routeURL)) {
			this.extractURLParameters(routeURL, request);
		}

		return result;
	}

	/**
	 * Maps the given URL/Method to a {@link Controller} class
	 * 
	 * @param url
	 *            the URL
	 * @param method
	 *            the {@link HTTPMethod}.
	 * @return The {@link Controller} class to handler the request
	 * @throws PageNotFoundExeption
	 *             If no {@link MetadataHandler} is found for the given URL/{@link HTTPMethod} pair
	 */
	protected ControllerDescriptor findController(String url, String method) throws NotFoundExeption {
		ControllerDescriptor result;

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
	private void extractURLParameters(String routeURL, HttpServletRequest request) {
		String[] mappingURLTokens = routeURL.split(SEPARATOR);
		String[] requestURLTokens = request.getRequestURI().split(SEPARATOR);
		for (int i = 1; i < mappingURLTokens.length; i++) {
			Matcher m = variablePattern.matcher(mappingURLTokens[i]);
			if (m.matches()) {
				String attName = mappingURLTokens[i];
				attName = attName.substring(1, attName.length() - 1);
				String value = requestURLTokens[i];
				request.setAttribute(attName, value);
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
	private ControllerDescriptor findDynamicURL(String key) {
		ControllerDescriptor result = null;
		for (Entry<String, ControllerDescriptor> entry : this.dynamicURLs.entrySet()) {
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
	private String getURLKey(String url, String method) {
		if (url.equals("")) {
			url = "/";
		}
		if (url.length() > 1 && url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url + METHOD_SEPARATOR + method;
	}

	/**
	 * Generates a regex to recognize the given dynamic URL.
	 * 
	 * @param url
	 *            the dynamicUrl to be transformed to a regex.
	 * @return a regex for the given URL.
	 */
	private String generateRegex(String url) {
		return variablePattern.matcher(url).replaceAll(Matcher.quoteReplacement("[a-zA-Z_0-9@.]+"));
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
