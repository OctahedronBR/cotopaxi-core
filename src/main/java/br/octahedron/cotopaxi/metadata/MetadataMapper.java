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
package br.octahedron.cotopaxi.metadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.octahedron.cotopaxi.CotopaxiConfigView;
import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;
import br.octahedron.cotopaxi.metadata.annotation.Response.ResponseMetadata;
import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.util.reflect.ReflectionUtil;

/**
 * This entity is responsible my map URL/HTTPMethod to a {@link MetadataHandler}.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class MetadataMapper {

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
	 * If no ModelMapping is found for the given url/method pair, an PageNotFoundException is
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

	// Cached URLS vs ModelMapping Class
	private Map<String, MetadataHandler> cachedURLs = new HashMap<String, MetadataHandler>();
	// static URLS vs ModelMapping Class
	private Map<String, MetadataHandler> staticURLs = new HashMap<String, MetadataHandler>();
	// dynamic URLS vs ModelMapping Class
	private Map<String, MetadataHandler> dynamicURLs = new HashMap<String, MetadataHandler>();
	// reverse Map: ModelMapping Class vs. Mapping URL
	private Map<MetadataHandler, String> urlMapping = new HashMap<MetadataHandler, String>();

	public MetadataMapper(CotopaxiConfigView configView) throws SecurityException, NoSuchMethodException {
		// loads the model metadata to mapper
		for (Class<?> facade : configView.getModelFacades()) {
			facade.getConstructor(new Class<?>[0]);
			ReflectionUtil.getAnnotatedMethods(facade, Action.class, new MetadataLoader(this));
		}
	}

	/**
	 * Adds a new mapping to this Mapper
	 * 
	 * @param mapping
	 *            the mapping to be added
	 */
	protected void add(MetadataHandler metadata) {
		String url = metadata.getActionMetadata().getUrl();
		HTTPMethod httpMethod = metadata.getActionMetadata().getHttpMethod();
		this.urlMapping.put(metadata, url);
		if (this.isStaticURL(url)) {
			if (httpMethod == HTTPMethod.GETANDPOST) {
				this.staticURLs.put(this.getURLKey(url, HTTPMethod.GET), metadata);
				this.staticURLs.put(this.getURLKey(url, HTTPMethod.POST), metadata);
			} else {
				this.staticURLs.put(this.getURLKey(url, httpMethod), metadata);
			}
		} else {
			// replace all the {var} to create a url regex and adds it to the dynamicUrlsMap
			String urlRegex = this.generateRegex(url);
			if (httpMethod == HTTPMethod.GETANDPOST) {
				this.dynamicURLs.put(this.getURLKey(urlRegex, HTTPMethod.GET), metadata);
				this.dynamicURLs.put(this.getURLKey(urlRegex, HTTPMethod.POST), metadata);
			} else {
				this.dynamicURLs.put(this.getURLKey(urlRegex, httpMethod), metadata);
			}
		}

	}

	/**
	 * Gets the {@link MetadataHandler} for the given URL and {@link HTTPMethod}.
	 * 
	 * @param request
	 *            The <code>RequestHandler</code>
	 * @return The {@link MetadataHandler} for the given URL/HTTPMethod.
	 * @throws PageNotFoundExeption
	 *             If no {@link MetadataHandler} is found for the given URL/{@link HTTPMethod} pair
	 */
	public MetadataHandler getMapping(RequestWrapper request) throws PageNotFoundExeption {
		// finds the metadata handler
		MetadataHandler metadata = this.getMapping(request.getURL(), request.getHTTPMethod());
		String mappingURL = this.urlMapping.get(metadata);
		// check format
		this.checkFormat(metadata.getResponseMetadata(), request);
		// extract url parameters and put it on request
		InputAdapter adapter = metadata.getActionMetadata().getInputAdapter();
		if (adapter.hasAttributes() && !this.isStaticURL(mappingURL)) {
			this.extractURLParameters(adapter, mappingURL, request);
		}
		// returns
		return metadata;
	}

	private void checkFormat(ResponseMetadata responseMetadata, RequestWrapper request) throws PageNotFoundExeption {
		String requiredFormat = request.getFormat();
		if (requiredFormat != null) {
			for (String acceptableFormat : responseMetadata.getFormats()) {
				if (!requiredFormat.equals(acceptableFormat)) {
					throw new PageNotFoundExeption(request.getURL(), request.getHTTPMethod());
				}
			}
		}
	}

	/**
	 * @see MetadataMapper#getMapping(ServletRequestWrapperImpl)
	 * @param url
	 *            the URL to be mapped
	 * @param method
	 *            the request HTTP method.
	 * @return The {@link MetadataHandler} for the given URL/HTTPMethod.
	 * @throws PageNotFoundExeption
	 *             If no {@link MetadataHandler} is found for the given URL/{@link HTTPMethod} pair
	 */
	protected MetadataHandler getMapping(String url, HTTPMethod method) throws PageNotFoundExeption {
		String key = this.getURLKey(url, method);

		MetadataHandler result;
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
			throw new PageNotFoundExeption(url, method);
		}
	}

	/**
	 * Extracts the dynamic request parameters on the URL. It checks the mapping URL pattern and the
	 * accessed URL and maps the parameters, setting it on the {@link RequestWrapper}.
	 */
	private void extractURLParameters(InputAdapter mapping, String mappingURL, RequestWrapper request) {
		String[] mappingURLTokens = mappingURL.split(SEPARATOR);
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
	 * Finds the mapping for a given url, if this URL represents a dynamic URL.
	 * 
	 * @param key
	 *            the URL key, generated using the
	 *            {@link MetadataMapper#getURLKey(String, HTTPMethod)} method.
	 * @return The {@link MetadataHandler} for the given URL, if exists, or <code>null</code>
	 *         otherwise.
	 */
	private MetadataHandler findDynamicURL(String key) {
		MetadataHandler result = null;
		for (Entry<String, MetadataHandler> entry : this.dynamicURLs.entrySet()) {
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
