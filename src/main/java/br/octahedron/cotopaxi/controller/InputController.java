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
import static java.util.Collections.list;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A base class that provides access to input and output attributes for a controller, but can't
 * render/redirect a response
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class InputController {

	protected static final String USERNAME_KEY = "{CURRENT_USER_NAME}";
	protected static final String USER_AUTHORIZED = "{IS_CURRENT_USER_AUTHORIZED}";

	/**
	 * Gets the {@link HttpServletRequest}
	 */
	protected final HttpServletRequest request() {
		return getContext().getRequest();
	}

	/**
	 * Gets all input names. Useful to iterate over all inputs and recover its values.
	 */
	@SuppressWarnings("unchecked")
	protected final Collection<String> input() {
		return list(request().getParameterNames());
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
	 * Gets the {@link ControllerDescriptor} for the current context
	 */
	protected final ControllerDescriptor controllerDescriptor() {
		return getContext().getControllerDescriptor();
	}

	/**
	 * Get a collection with all client's acceptable locales, sorted by preference order.
	 */
	@SuppressWarnings("unchecked")
	protected final Collection<Locale> locales() {
		return list(this.request().getLocales());
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
	 * @param shouldTrim
	 *            <code>true</code> if should remove leading and trailing white spaces,
	 *            <code>false</code> to return the raw String
	 * @return The parameter's value if exists, or <code>null</code> if there's no input parameter
	 *         with the given name.
	 */
	protected final String in(String name, boolean shouldTrim) {
		HttpServletRequest request = this.request();
		String result = request.getParameter(name);
		if (result == null || result.equals("")) {
			result = (String) request.getAttribute(name);
		}
		return (result != null && shouldTrim) ? result.trim() : result;
	}

	/**
	 * Get an input parameter with the given key, and convert it using the given converter.
	 * 
	 * Input parameter can be parameters passed using both POST and GET method, or parameters passed
	 * at the url address, with leading and trailing white spaces removed;
	 * 
	 * @param name
	 *            The parameter's name
	 * @param converter
	 *            The converter to be used to convert input value
	 * @return The parameter's value if exists, or <code>null</code> if there's no input parameter
	 *         with the given name, or if converter can not convert the input.
	 * 
	 * @see Converter
	 */
	protected final <T> T in(String name, Converter<T> converter) {
		return converter.convert(this.in(name, true));
	}

	// protected final <T> T in(Class<T> wrapperClass) {
	// return null;
	// }

	/**
	 * Get all the values for a input parameter with the given key. It's useful for checkbox input,
	 * for example.
	 * 
	 * If there's no value for the given input parameter, returns an empty collection.
	 * 
	 * @param name
	 *            The parameter's name
	 * @return A {@link Collection} with all values for the given parameter.
	 */
	@SuppressWarnings("unchecked")
	protected final Collection<String> values(String name) {
		String[] values = this.request().getParameterValues(name);
		return (Collection<String>) ((values != null) ? Arrays.asList(values) : Collections.emptyList());
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
	 * Gets the current user for request, if exists.
	 * 
	 * This method is useful for authentication mechanisms
	 * 
	 * @return the user's name, or <code>null</code> if not set
	 */
	protected final String currentUser() {
		return (String) this.session(USERNAME_KEY);
	}

	/**
	 * Verify if this request was marked as authorized
	 * 
	 * This method is useful for authentication mechanisms
	 * 
	 * @return <code>true</code> if the request was marked as authorized, <code>false</code>
	 *         otherwise.
	 */
	protected final boolean isAuthorized() {
		return Boolean.parseBoolean((String) this.request().getAttribute(USER_AUTHORIZED));
	}

	/**
	 * Checks if the request was already answered
	 */
	protected final boolean isAnswered() {
		return getContext().isAnswered();
	}
}
