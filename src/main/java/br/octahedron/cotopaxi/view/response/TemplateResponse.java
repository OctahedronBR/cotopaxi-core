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
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.controller.ControllerContext;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.view.OutputStreamBuilder;
import br.octahedron.cotopaxi.view.render.TemplateRender;

/**
 * A {@link RenderableResponse} that render and write templates.
 * 
 * @see {@link TemplateRender}
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class TemplateResponse extends RenderableResponse {

	@Inject
	private TemplateRender templateRender;

	private OutputStreamBuilder builder = null;
	private String template;

	/**
	 * @param template
	 *            The template to be rendered
	 * @param code
	 *            The HTTP result code
	 * @param context
	 *            The request {@link ControllerContext}
	 */
	public TemplateResponse(String template, int code, ControllerContext context) {
		super(code, context);
		this.template = template;
	}

	/**
	 * @param template
	 *            The template to be rendered
	 * @param code
	 *            The HTTP result code
	 * @param output
	 *            The output objects map
	 * @param locale
	 *            The response locale
	 */
	public TemplateResponse(String template, int code, Map<String, Object> output, Locale locale) {
		super(code, output, locale);
		this.template = template;
	}

	/**
	 * @param templateRender
	 *            the templateRender to set
	 */
	public void setTemplateRender(TemplateRender templateRender) {
		this.templateRender = templateRender;
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
		OutputStream outStream = servletResponse.getOutputStream();
		if (this.builder != null) {
			outStream = this.builder.createOutputStream(outStream);
		}

		return outStream;
	}

	/**
	 * Sets the {@link OutputStreamBuilder} to be used to create the {@link OutputStream}
	 * 
	 * @param outputStreamBuilder
	 */
	public void setOutputStreamBuilder(OutputStreamBuilder outputStreamBuilder) {
		this.builder = outputStreamBuilder;
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
	 * Adds a new header to output. If there's already exists a header for the given key, the
	 * original one is kept.
	 * 
	 * @param key
	 *            The headers's key
	 * 
	 * @param value
	 *            The headers's value
	 */
	public void addHeader(String key, String value) {
		if (!this.headers.containsKey(key)) {
			this.headers.put(key, value);
		}
	}

	/**
	 * Add a cookie.
	 * 
	 * @param name
	 *            a String specifying the name of the cookie
	 * @param value
	 *            a String specifying the value of the cookie
	 * 
	 * @see Cookie
	 */
	public void addCookie(String name, String value) {
		this.addCookie(name, value, null, -1);
	}

	/**
	 * Add a cookie.
	 * 
	 * @param name
	 *            a String specifying the name of the cookie
	 * @param value
	 *            a String specifying the value of the cookie
	 * @param domain
	 *            a String containing the domain name within which this cookie is visible; form is
	 *            according to RFC 2109
	 * 
	 * @see Cookie
	 */
	public void addCookie(String name, String value, String domain) {
		this.addCookie(name, value, domain, -1);
	}

	/**
	 * Add a cookie.
	 * 
	 * @param name
	 *            a String specifying the name of the cookie
	 * @param value
	 *            a String specifying the value of the cookie
	 * @param maxAge
	 *            an integer specifying the maximum age of the cookie in seconds; if negative, means
	 *            the cookie is not stored; if zero, deletes the cookie
	 * 
	 * @see Cookie
	 */
	public void addCookie(String name, String value, int maxAge) {
		this.addCookie(name, value, null, maxAge);
	}

	/**
	 * Adds a cookie.
	 * 
	 * @param name
	 *            a String specifying the name of the cookie
	 * @param value
	 *            a String specifying the value of the cookie
	 * @param domain
	 *            a String containing the domain name within which this cookie is visible; form is
	 *            according to RFC 2109
	 * @param maxAge
	 *            an integer specifying the maximum age of the cookie in seconds; if negative, means
	 *            the cookie is not stored; if zero, deletes the cookie
	 * 
	 * @see Cookie
	 */
	public void addCookie(String name, String value, String domain, int maxAge) {
		Cookie c = new Cookie(name, value);
		if (domain != null) {
			c.setDomain(domain);
		}
		if (maxAge != -1) {
			c.setMaxAge(maxAge);
		}
		this.cookies.add(c);
	}

	/**
	 * Gets the current locale to be used to render this template
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * Set's the locale to be used to render this template.
	 */
	public void setLocale(Locale lc) {
		this.locale = lc;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public String getContentType() {
		Charset cs = charset();
		return "text/html; charset= " + cs.name();
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void render() {
		this.templateRender.render(this.template, this.output, this.writer);
	}
}
