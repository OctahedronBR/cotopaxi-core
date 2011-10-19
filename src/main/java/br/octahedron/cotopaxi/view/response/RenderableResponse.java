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
package br.octahedron.cotopaxi.view.response;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.controller.ControllerResponse;

/**
 * A {@link ControllerResponse} that can be intercepted.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class RenderableResponse extends ServletGenericResponse {

	protected Writer writer;
	protected int code;
	protected Map<String, String> cookies;
	protected Map<String, String> headers;
	protected Locale locale;
	protected Map<String, Object> output;

	/**
	 * @param subClass
	 */
	public RenderableResponse(int code, Map<String, Object> output, Map<String, String> cookies, Map<String, String> headers, Locale locale) {
		this.code = code;
		this.output = output;
		this.cookies = cookies;
		this.headers = headers;
		this.locale = locale;
	}

	@Override
	public final void dispatch(HttpServletResponse servletResponse) throws IOException {
		this.writer = servletResponse.getWriter();

		// adjust headers
		if (this.headers != null) {
			for (Entry<String, String> entry : this.headers.entrySet()) {
				servletResponse.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// adjust cookies
		if (cookies != null) {
			for (Entry<String, String> entry : cookies.entrySet()) {
				servletResponse.addCookie(new Cookie(entry.getKey(), entry.getValue()));
			}
		}
		// set locale
		servletResponse.setLocale(this.locale);
		// set content type
		servletResponse.setContentType(this.getContentType());
		// set status code
		servletResponse.setStatus(this.code);

		// render output
		this.render();

		// flush, if necessary
		if (servletResponse.isCommitted()) {
			servletResponse.flushBuffer();
		}
		this.writer = null;
	}

	/**
	 * Gets this Response's Writer to be used to render. If this method is called out of the
	 * dispatch execution scope, it throws an IllegalStateException.
	 * 
	 * This method should be called only be ResponseInterceptors
	 * 
	 * @return The writer to be used to render.
	 */
	public Writer getWriter() {
		if (this.writer != null) {
			return this.writer;
		} else {
			throw new IllegalStateException("Not executing dispatch method");
		}
	}

	/**
	 * Sets the writer to be used to render this response
	 * 
	 * @param writer
	 *            the writer to be used to render this response
	 */
	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	/**
	 * Adds a new object to output. If there's already exists an object for the given key, the
	 * original one is kept.
	 * 
	 * @param key
	 *            The object's key
	 * 
	 * @param value
	 *            The object's value
	 */
	public void addOutput(String key, Object value) {
		if (!this.output.containsKey(key)) {
			this.output.put(key, value);
		}
	}

	/**
	 * Gets this response ContentType
	 */
	protected abstract String getContentType();

	/**
	 * Renders this response
	 */
	protected abstract void render();

}
