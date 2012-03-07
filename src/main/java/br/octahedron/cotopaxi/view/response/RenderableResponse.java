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

import static br.octahedron.cotopaxi.CotopaxiProperty.charset;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.controller.ControllerContext;
import br.octahedron.cotopaxi.controller.ControllerResponse;

/**
 * A {@link ControllerResponse} that can be intercepted.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class RenderableResponse extends ServletGenericResponse {

	protected Writer writer;
	protected int code;
	protected Set<Cookie> cookies;
	protected Map<String, String> headers;
	protected Locale locale;
	protected Map<String, Object> output;

	/**
	 * @param code
	 *            The HTTP response code
	 * @param context
	 *            The request {@link ControllerContext}
	 */
	protected RenderableResponse(int code, ControllerContext context) {
		this(code, context.getOutput(), context.getCookies(), context.getHeaders(), context.getLocale());
	}

	/**
	 * @param code
	 *            The HTTP response code
	 * @param output
	 *            The output objects map
	 * @param locale
	 *            The output locale
	 */
	protected RenderableResponse(int code, Map<String, Object> output, Locale locale) {
		this(code, output, new HashSet<Cookie>(), new HashMap<String, String>(), locale);
	}

	/**
	 * @param code
	 *            The HTTP response code
	 * @param output
	 *            The output objects map
	 * @param cookies
	 *            The response cookies
	 * @param headers
	 *            The response headers
	 * @param locale
	 *            The output locale
	 */
	protected RenderableResponse(int code, Map<String, Object> output, Set<Cookie> cookies, Map<String, String> headers, Locale locale) {
		this.code = code;
		this.output = output;
		this.cookies = cookies;
		this.headers = headers;
		this.locale = locale;
	}

	/**
	 * Gets the {@link OutputStream} to be used to write output.
	 * 
	 * @param servletResponse
	 *            The {@link ServletResponse} to be used to create the {@link OutputStream}
	 * @return The {@link OutputStream} to be used to write response.
	 * 
	 * @throws IOException
	 *             If some error occurs loading the {@link OutputStream}
	 */
	protected OutputStream getOutputStream(HttpServletResponse servletResponse) throws IOException {
		return servletResponse.getOutputStream();
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public final void dispatch(HttpServletResponse servletResponse) throws IOException {
		this.writer = new OutputStreamWriter(this.getOutputStream(servletResponse), charset());

		// adjust headers
		if (this.headers != null) {
			for (Entry<String, String> entry : this.headers.entrySet()) {
				servletResponse.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// adjust cookies
		if (cookies != null) {
			for (Cookie c : cookies) {
				servletResponse.addCookie(c);
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
		this.writer.close();
		this.writer = null;
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
