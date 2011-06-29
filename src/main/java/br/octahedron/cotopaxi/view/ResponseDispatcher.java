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
package br.octahedron.cotopaxi.view;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.controller.ControllerResponse;
import br.octahedron.cotopaxi.interceptor.InterceptorManager;
import br.octahedron.cotopaxi.view.response.RedirectResponse;
import br.octahedron.cotopaxi.view.response.WriteableResponse;
import br.octahedron.util.Log;

/**
 * This entity dispatch {@link ControllerResponse}
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ResponseDispatcher {

	private static final Log log = new Log(ResponseDispatcher.class);
	private InterceptorManager interceptor;

	public ResponseDispatcher(InterceptorManager interceptor) {
		this.interceptor = interceptor;
	}

	/**
	 * Dispatches the {@link ControllerResponse}
	 * @throws IOException 
	 */
	public void dispatch(ControllerResponse controllerResponse, HttpServletResponse response) throws IOException {
		if ( controllerResponse instanceof RedirectResponse) {
			this.dispatch((RedirectResponse)controllerResponse, response);
		} else if (controllerResponse instanceof WriteableResponse) {
			this.dispatch((WriteableResponse)controllerResponse, response);
		} else {
			throw new IllegalArgumentException("The given ControllerResponse class is Unknown: " + controllerResponse.getClass().getName());
		}
	}

	/**
	 * Dispatches a {@link RedirectResponse}
	 */
	private void dispatch(RedirectResponse response, HttpServletResponse servletResponse) throws IOException {
		log.debug("Sending redirect to %s", response.getUrl());
		servletResponse.sendRedirect(response.getUrl());
	}

	/**
	 * Dispatches a {@link WriteableResponse}
	 */
	private void dispatch(WriteableResponse response, HttpServletResponse servletResponse) throws IOException {
		// adjust headers
		Map<String, String> headers = response.getHeaders();
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				servletResponse.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// adjust cookies
		Map<String, String> cookies = response.getCookies();
		if (cookies != null) {
			for (Entry<String, String> entry : cookies.entrySet()) {
				servletResponse.addCookie(new Cookie(entry.getKey(), entry.getValue()));
			}
		}
		// set locale
		servletResponse.setLocale(response.getLocale());
		// set content type
		servletResponse.setContentType(response.getContentType());
		// set status code
		servletResponse.setStatus(response.getCode());
		// write response
		log.debug("Writing response to client");
		Writer writer = this.interceptor.getWriter(servletResponse.getWriter());
		response.writeResponse(writer);
		// flush
		if (servletResponse.isCommitted()) {
			servletResponse.flushBuffer();
		}
		this.interceptor.finish();
	}
}
