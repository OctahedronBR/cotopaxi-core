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
package br.octahedron.cotopaxi.test;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.controller.ControllerResponse;
import br.octahedron.cotopaxi.view.response.JSONResponse;
import br.octahedron.cotopaxi.view.response.RedirectResponse;
import br.octahedron.cotopaxi.view.response.RenderableResponse;
import br.octahedron.cotopaxi.view.response.SimpleTextResponse;
import br.octahedron.cotopaxi.view.response.TemplateResponse;

/**
 * Encapsulates a response to be used by tests. This class provides methods to check if the response
 * generates by a controller execution is as expected.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class Response {

	private ControllerResponse response;

	/**
	 * Creates a new Response from a {@link ControllerResponse}
	 */
	public Response(ControllerResponse resp) {
		this.response = resp;
	}

	/**
	 * Checks if the response is a redirect.
	 * 
	 * @return <code>true</code> if is a redirect, <code>false</code> otherwise
	 * 
	 * @see {@link Controller#redirect}
	 */
	public boolean isRedirect() {
		return this.response instanceof RedirectResponse;
	}

	/**
	 * Checks if the response is a JSON response.
	 * 
	 * @return <code>true</code> if is a JSON, <code>false</code> otherwise
	 * 
	 * @see {@link Controller#asJSON}
	 */
	public boolean isJSON() {
		return this.response instanceof JSONResponse;
	}

	/**
	 * Checks if the response is a Template response.
	 * 
	 * @return <code>true</code> if is a template, <code>false</code> otherwise
	 * 
	 * @see {@link Controller#render}
	 */
	public boolean isTemplate() {
		return this.response instanceof TemplateResponse;
	}

	/**
	 * Checks if the response is a simple Text response.
	 * 
	 * @return <code>true</code> if is a text, <code>false</code> otherwise
	 * 
	 * @see {@link Controller#sucess()}
	 */
	public boolean isText() {
		return this.response instanceof SimpleTextResponse;
	}

	/**
	 * Checks if exists a cookie with the given name
	 * 
	 * @param name
	 *            The cookie's name
	 * @return <code>true</code> if exists a cookie with the given name, <code>false</code>
	 *         otherwise
	 */
	public boolean existsCookie(String name) {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.cookie(name) != null;
		} else {
			return false;
		}
	}

	/**
	 * Checks if exists a header with the given name
	 * 
	 * @param name
	 *            The header's name
	 * @return <code>true</code> if exists a header with the given name, <code>false</code>
	 *         otherwise
	 */
	public boolean existsHeader(String name) {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.header(name) != null;
		} else {
			return false;
		}
	}

	/**
	 * Checks if exists an output with the given name
	 * 
	 * @param name
	 *            The output's name
	 * @return <code>true</code> if exists an output with the given name, <code>false</code>
	 *         otherwise
	 */
	public boolean existsOutput(String name) {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.output(name) != null;
		} else {
			return false;
		}
	}

	/**
	 * Gets the response result code. Eg.: 200 for success, 302 for redirect, 404 for not found, 400
	 * for bad input/invalid
	 * 
	 * @return The response result code
	 */
	public int code() {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.code();
		} else {
			return 302;
		}
	}

	/**
	 * Gets the template name used to render this response. Only works if this response is a
	 * template response.
	 * 
	 * @return The template name for this response, if this response is a template response. Returns
	 *         null if it isn't a template response.
	 * 
	 * @see Response#isTemplate()
	 */
	public String template() {
		if (this.response instanceof TemplateResponse) {
			TemplateResponse resp = (TemplateResponse) this.response;
			return resp.template();
		} else {
			return null;
		}
	}

	/**
	 * Gets the redirect url for this response. Only works if this response is a redirect response.
	 * 
	 * @return The redirect url or <code>null<code>.
	 * 
	 * @see Response#isRedirect()
	 */
	public String redirect() {
		if (this.response instanceof RedirectResponse) {
			RedirectResponse resp = (RedirectResponse) this.response;
			return resp.redirectUrl();
		} else {
			return null;
		}
	}

	/**
	 * Gets a cookie's value
	 * 
	 * @param name
	 *            the cookie's name
	 * @return the cookie's value, if exists a cookie with the given name, or <code>null</code> if
	 *         there's no cookie with such name
	 */
	public String cookie(String name) {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.cookie(name);
		} else {
			return null;
		}
	}

	/**
	 * Gets a header's value
	 * 
	 * @param name
	 *            the header's name
	 * @return the header's value, if exists a header with the given name, or <code>null</code> if
	 *         there's no header with such name
	 */
	public String header(String name) {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.header(name);
		} else {
			return null;
		}
	}

	/**
	 * Gets an output's value
	 * 
	 * @param name
	 *            the output's name
	 * @return the output's value, if exists an output with the given name, or <code>null</code> if
	 *         there's no output with such name
	 */
	public Object output(String name) {
		if (this.response instanceof RenderableResponse) {
			RenderableResponse resp = (RenderableResponse) this.response;
			return resp.output(name);
		} else {
			return null;
		}
	}
}
