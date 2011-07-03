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

import static br.octahedron.cotopaxi.CotopaxiProperty.ERROR_TEMPLATE;
import static br.octahedron.cotopaxi.CotopaxiProperty.FORBIDDEN_TEMPLATE;
import static br.octahedron.cotopaxi.CotopaxiProperty.INVALID_TEMPLATE;
import static br.octahedron.cotopaxi.CotopaxiProperty.NOT_FOUND_TEMPLATE;
import static br.octahedron.cotopaxi.CotopaxiProperty.getProperty;
import static br.octahedron.cotopaxi.controller.ControllerContext.threadContexts;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The base class for Controllers and Middleware controller.
 * 
 * It provides access to input, session, header and cookies parameters/attributes, methods to
 * redirect and render output.
 * 
 * To implement this class you should extends this class and implement your handlers methods using
 * "<method name><controller name>", e.g., "getUsers()". Your controller methods should has no
 * parameters and should return void. To return results, simple call the
 * {@link Controller#out(String, Object)} method to add an Object to output and after processing
 * call the method {@link Controller#render(String, int)} or one of the shortcut methods (success,
 * error, notFound, forbidden, invalid).
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class Controller {
	
	/*
	 * Internal methods
	 */

	/**
	 * Gets the {@link HttpServletRequest}
	 */
	private final HttpServletRequest request() {
		return threadContexts.get().getRequest();
	}

	/**
	 * Checks if the request was already answered
	 */
	private final boolean isAnswered() {
		return threadContexts.get().isAnswered();
	}

	/**
	 * Gets the output objects map
	 */
	private final Map<String, Object> output() {
		return threadContexts.get().getOutput();
	}

	/**
	 * Gets the output cookies map
	 */
	private final Map<String, String> cookies() {
		return threadContexts.get().getCookies();
	}

	/**
	 * Gets the output headers map
	 */
	private final Map<String, String> headers() {
		return threadContexts.get().getHeaders();
	}
	
	/**
	 * Gets the controller name
	 */
	protected final String controllerName() {
		return threadContexts.get().getControllerName();
	}
	
	/**
	 * Gets the requested relative URL
	 * 
	 * E.g.: /dashboard
	 * 
	 * @return the requested URL
	 */
	protected final String requestedUrl() {
		return this.request().getRequestURI();
	}

	/**
	 * Get an input parameter with the given key.
	 * 
	 * Input parameter can be parameters passed using both POST and GET method, or parameters passed
	 * at the url address;
	 * 
	 * @param name
	 *            The parameter's name
	 * @return The parameter's value if exists, or <code>null</code> if there's no input parameter
	 *         with the given name.
	 */
	protected final String in(String name) {
		HttpServletRequest request = this.request();
		String result = request.getParameter(name);
		if (result == null || result.equals("")) {
			result = (String) request.getAttribute(name);
		}
		return result;
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

	/**
	 * Render the given template with SUCCESS (200) code
	 * 
	 * The same as call render(template, 200)
	 */
	protected final void success(String template) {
		this.render(template, 200);
	}

	/**
	 * Render the given template with SERVER ERROR (500) code
	 * 
	 * The same as call render(template, 500)
	 */
	protected final void error(String template) {
		this.render(template, 500);
	}
	
	/**
	 * Render the default SERVER ERROR (500) page
	 */
	protected final void error() {
		this.render(getProperty(ERROR_TEMPLATE),500);
	}

	/**
	 * Render the given template with NOT FOUND (404) code
	 * 
	 * The same as call render(template, 404)
	 */
	protected final void notFound(String template) {
		this.render(template, 404);
	}
	
	/**
	 * Render the default NOT FOUND (404) page
	 */
	protected final void notFound() {
		this.render(getProperty(NOT_FOUND_TEMPLATE),404);
	}

	/**
	 * Render the given template with FORBIDDEN (403) code
	 * 
	 * The same as call render(template, 403)
	 */
	protected final void forbidden(String template) {
		this.render(template, 403);
	}
	
	/**
	 * Render the default FORBIDDEN (403) page
	 */
	protected final void forbidden() {
		this.render(getProperty(FORBIDDEN_TEMPLATE),403);
	}

	/**
	 * Render the given template with BAD REQUEST (400) code
	 * 
	 * The same as call render(template, 400)
	 */
	protected final void invalid(String template) {
		this.render(template, 400);
	}
	
	/**
	 * Render the default BAD REQUEST (400) page
	 */
	protected final void invalid() {
		this.render(getProperty(INVALID_TEMPLATE),400);
	}

	/**
	 * Renders the given template. It will use the objects set using the
	 * {@link Controller#out(String, Object)} to render the template. After render, the code
	 * continues to execute, and the code will be written to client only after the
	 * {@link Controller} execution flow ends.
	 * 
	 * @param template
	 *            the template file to be rendered
	 * @param code
	 *            the http code
	 * 
	 * @throws IllegalStateException
	 */
	protected final void render(String template, int code) {
		if (!this.isAnswered()) {
			threadContexts.get().render(template, code);
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}

	/**
	 * Redirects to the given url
	 * 
	 * @param dest
	 *            The redirect destination
	 */
	protected final void redirect(String dest) {
		if (!this.isAnswered()) {
			threadContexts.get().redirect(dest);
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}

	protected final void asJson(int code) {
		if (!this.isAnswered()) {
			throw new UnsupportedOperationException("Method not implemented yet. Maybe on future releases");
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}
}
