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

import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import br.octahedron.cotopaxi.controller.ControllerResponse;

/**
 * A response that writes content to user
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class WriteableResponse implements ControllerResponse {
	
	private int code;
	private Map<String, String> cookies;
	private Map<String, String> headers;
	private Locale locale;
	protected Map<String, Object> output;

	public WriteableResponse(int code, Map<String, Object> output, Map<String, String> cookies, Map<String, String> headers, Locale locale) {
		this.code = code;
		this.output = output;
		this.cookies = cookies;
		this.headers = headers;
		this.locale = locale;
	}
	
	/**
	 * Get the response cookies, if exists, or null if doesn't exists
	 */
	public final Map<String, String> getCookies() {
		return this.cookies;
	}
	
	/**
	 * Get the response headers, if exists, or null if doesn't exists
	 */
	public final Map<String, String> getHeaders() {
		return this.headers;
	}
	
	/**
	 * Gets the response {@link Locale}
	 */
	public final Locale getLocale() {
		return this.locale;
	}
	
	/**
	 * Gets the Http Status Code for this response
	 */
	public final int getCode() {
		return this.code;
	}

	/**
	 * Writes the response using the given {@link Writer}
	 * @param writer the writer which response should be written
	 */
	public abstract void writeResponse(Writer writer);
	
	/**
	 * Gets the response content type. E.g.: "text/html" 
	 */
	public abstract String getContentType();
}
