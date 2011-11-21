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
import static br.octahedron.cotopaxi.controller.ControllerContext.getContext;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import br.octahedron.cotopaxi.view.response.JSONResponse;
import br.octahedron.cotopaxi.view.response.RedirectResponse;
import br.octahedron.cotopaxi.view.response.TemplateResponse;

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
public abstract class Controller extends InputController {
	
	/**
	 * Gets the output cookies map
	 */
	protected final Map<String, String> cookies() {
		return getContext().getCookies();
	}

	/**
	 * Gets the output headers map
	 */
	protected final Map<String, String> headers() {
		return getContext().getHeaders();
	}
	

	/**
	 * Gets the output objects map
	 */
	protected final Map<String, Object> output() {
		return getContext().getOutput();
	}
	
	/**
	 * Sets the response's locale
	 * @param lc the response's locale
	 */
	protected final void locale(Locale lc) {
		getContext().setLocale(lc);
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
		if (value != null) {
			this.request().getSession(true).setAttribute(key, value);
		} else {
			HttpSession session = this.request().getSession(false);
			if (session != null) {
				session.removeAttribute(key);
			}
		}
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
	 * It echos the input parameters to output, using the same names and values.
	 * 
	 * If the parameter has multiple values it echos the values as a String Array. If the parameter
	 * has no value, it echos a blank ("") String.
	 */
	@SuppressWarnings("rawtypes")
	protected final void echo() {
		Map parameters = this.request().getParameterMap();
		Iterator keys = parameters.keySet().iterator();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String[] values = (String[]) parameters.get(key);
			if (values.length == 1) {
				this.out(key, values[0]);
			} else if (values.length > 1) {
				this.out(key, values);
			} else {
				this.out(key, "");
			}
		}
	}

	/**
	 * Sets the given user name as the current user.
	 * 
	 * This method is useful for authentication mechanisms
	 */
	protected final void currentUser(String username) {
		this.session(USERNAME_KEY, username);
	}

	/**
	 * Mark the request as authorized
	 * 
	 * This method is useful for authorization mechanisms
	 */
	protected final void authorized() {
		this.request().setAttribute(USER_AUTHORIZED, "true");
	}

	/**
	 * Sets the {@link ControllerResponse} for the current request. This method should be used only
	 * by {@link Controller} extensions, avoid use this method directly when implementing a
	 * controller. 
	 */
	protected final void setControllerResponse(ControllerResponse response) {
		getContext().setControllerResponse(response);
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
		this.render(getProperty(ERROR_TEMPLATE), 500);
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
		this.render(getProperty(NOT_FOUND_TEMPLATE), 404);
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
		this.render(getProperty(FORBIDDEN_TEMPLATE), 403);
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
		this.render(getProperty(INVALID_TEMPLATE), 400);
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
			this.setControllerResponse(new TemplateResponse(template, code, getContext()));
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}

	/**
	 * Renders {@link Controller#out(String, Object)} objects with SUCCESS (200) code
	 * 
	 * The same as call asJSON(200)
	 */
	protected final void jsonSuccess() {
		this.asJSON(200);
	}

	/**
	 * Renders {@link Controller#out(String, Object)} objects with BAD REQUEST (400) code
	 * 
	 * The same as call asJSON(400)
	 */
	protected final void jsonInvalid() {
		this.asJSON(400);
	}

	/**
	 * Renders the objects set using the {@link Controller#out(String, Object)} as JSON format.
	 * After render, the code continues to execute, and the code will be written to client only
	 * after the {@link Controller} execution flow ends.
	 * 
	 * @param code
	 *            the http code
	 * 
	 * @throws IllegalStateException
	 */
	protected final void asJSON(int code) {
		if (!this.isAnswered()) {
			this.setControllerResponse(new JSONResponse(code, getContext()));
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
			getContext().setControllerResponse(new RedirectResponse(dest));
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}
}
