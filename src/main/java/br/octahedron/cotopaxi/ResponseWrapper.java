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
package br.octahedron.cotopaxi;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.view.ContentType;
import br.octahedron.cotopaxi.view.formatter.Formatter;
import br.octahedron.cotopaxi.view.response.ResultCode;

/**
 * Encapsulates the response and provides access to all methods necessary to deliver the response to
 * user.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ResponseWrapper {

	private HttpServletResponse response;

	public ResponseWrapper(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * @param code
	 *            the response's status code
	 */
	public void setResultCode(ResultCode code) {
		this.response.setStatus(code.getIntCode());
	}

	/**
	 * Set response header's attributes.
	 * 
	 * @param key
	 *            header's attribute key.
	 * @param value
	 *            header's attribute value.
	 */
	public void setHeaderAttribute(String key, String value) {
		this.response.addHeader(key, value);
	}

	/**
	 * @param contentType
	 *            the response's content type
	 */
	public void setContentType(ContentType contentType) {
		this.response.setContentType(contentType.getContentTypeAsString());
	}

	/**
	 * @param locale
	 *            the response's locale
	 */
	public void setLocale(Locale locale) {
		this.response.setLocale(locale);
	}

	/**
	 * @param encoding
	 *            the response's charset (encoding)
	 */
	public void setCharacterEncoding(String encoding) {
		this.response.setCharacterEncoding(encoding);
	}

	/**
	 * Redirects the response to the given URL
	 * 
	 * @param url
	 *            The URL to be redirected
	 */
	public void redirect(String url) throws IOException {
		this.response.sendRedirect(url);
	}

	/**
	 * Renders the response using the given formatter and flush it to user. The response become
	 * invalid after this method invocation.
	 * 
	 * @param formatter
	 *            The formatter used to format the response to be renderer to user.
	 */
	public void render(Formatter formatter) throws IOException {
		this.setContentType(formatter.getContentType());
		this.setLocale(formatter.getLocale());
		this.render(formatter.getFormatted());
	}

	/**
	 * Renders the given content and flush it to user. The response become invalid after this method
	 * invocation.
	 * 
	 * @param output
	 *            The content to be renderer to user.
	 */
	public void render(String output) throws IOException {
		this.response.getWriter().write(output);
		this.response.flushBuffer();
	}
}
