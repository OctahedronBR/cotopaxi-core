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
package br.octahedron.cotopaxi.controller;

import static br.octahedron.cotopaxi.controller.ControllerContext.getContext;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A base class that provides access to input and output attributes for a controller, but can't render/redirect a response
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class BaseController {
	/**
	 * Gets the {@link HttpServletRequest}
	 */
	private final HttpServletRequest request() {
		return getContext().getRequest();
	}

	/**
	 * Gets the output objects map
	 */
	private final Map<String, Object> output() {
		return getContext().getOutput();
	}

	/**
	 * Gets the output cookies map
	 */
	private final Map<String, String> cookies() {
		return getContext().getCookies();
	}

	/**
	 * Gets the output headers map
	 */
	private final Map<String, String> headers() {
		return getContext().getHeaders();
	}

	/**
	 * Gets the server name. E.g.: www.octahedron.com.br
	 */
	protected final String serverName() {
		return this.request().getServerName();
	}
	
	/**
	 * Gets the lower sub-domain name. 
	 * 
	 * E.g.: for server name 'tech.octahedron.com.br' it returns 'tech' 
	 */
	protected final String subDomain() {
		return this.request().getServerName().split("\\.")[0];
	}
	
	/**
	 * Gets the controller name
	 */
	protected final String controllerName() {
		return getContext().getControllerName();
	}
	
	/**
	 * Gets the requested relative URL
	 * 
	 * E.g.: /dashboard
	 * 
	 * @return the requested URL
	 */
	protected final String fullRequestedUrl() {
		return this.request().getRequestURL().toString();
	}
	
	/**
	 * Gets the requested relative URL
	 * 
	 * E.g.: /dashboard
	 * 
	 * @return the requested URL
	 */
	protected final String relativeRequestedUrl() {
		return this.request().getRequestURI();
	}

	/**
	 * Get an input parameter with the given key.
	 * 
	 * Input parameter can be parameters passed using both POST and GET method, or parameters passed
	 * at the url address, with leading and trailing white spaces removed;
	 * 
	 * The same as call in(name,true);
	 * 
	 * @param name
	 *            The parameter's name
	 * @return The parameter's value if exists, or <code>null</code> if there's no input parameter
	 *         with the given name.
	 *         
	 * @see Controller#in(String, boolean)
	 */
	protected final String in(String name) {
		return this.in(name, true);
	}
	
	/**
	 * Get an input parameter with the given key.
	 * 
	 * Input parameter can be parameters passed using both POST and GET method, or parameters passed
	 * at the url address;
	 * 
	 * @param name
	 *            The parameter's name
	 * @param shouldTrim <code>true</code> if should remove leading and trailing white spaces, <code>false</code> to return the raw String
	 * @return The parameter's value if exists, or <code>null</code> if there's no input parameter
	 *         with the given name.
	 */
	protected final String in(String name, boolean shouldTrim) {
		HttpServletRequest request = this.request();
		String result = request.getParameter(name);
		if (result == null || result.equals("")) {
			result = (String) request.getAttribute(name);
		}
		return (result != null && shouldTrim) ? result.trim(): result;
	}

	/**
	 * Gets an object, with the given key, from session.
	 * 
	 * @param key
	 *            the object's key
	 * @return The object with the given key if exists or <code>null</code> if there's no object in
	 *         the session with the given key.
	 */
	protected final Object session(String key) {
		HttpSession session = this.request().getSession(false);
		if (session != null) {
			return session.getAttribute(key);
		} else {
			return null;
		}
	}

	/**
	 * Gets an header attribute
	 * 
	 * @param name
	 *            the header's name
	 * @return The header's value if exists or <code>null</code> if there's no header with the given
	 *         name.
	 */
	protected final String header(String name) {
		return this.request().getHeader(name);
	}

	/**
	 * Gets a cookie's value
	 * 
	 * @param name
	 *            the cookie's name
	 * @return the cookie's value if exists or <code>null</code> if theres no cookie with the given
	 *         name
	 */
	protected final String cookie(String name) {
		Cookie[] cookies = this.request().getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Stores an object in the session. If already exists an object stored with the given key, it
	 * will be overwritten
	 * 
	 * @param key
	 *            the object's key
	 * @param value
	 *            the object
	 */
	protected final void session(String key, Object value) {
		this.request().getSession(true).setAttribute(key, value);
	}

	/**
	 * Adds an object to the output. This objects will be used to by the view to render output.
	 * 
	 * @param key
	 *            the output's key
	 * @param value
	 *            the output's value
	 */
	protected final void out(String key, Object value) {
		this.output().put(key, value);
	}

	/**
	 * Sets a response's header.
	 * 
	 * @param name
	 *            the header's name
	 * @param value
	 *            the header's value
	 */
	protected final void header(String name, String value) {
		this.headers().put(name, value);
	}

	/**
	 * Set's a response cookie
	 * 
	 * @param name
	 *            the cookie's name
	 * @param value
	 *            the cookie's value
	 */
	protected final void cookie(String name, String value) {
		this.cookies().put(name, value);
	}	
}
