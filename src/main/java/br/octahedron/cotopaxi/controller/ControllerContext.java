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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Holds the current context for a {@link Controller}. It means that this class holds all necessary
 * information for a {@link Controller} flow, including the current request and response, the output
 * value, and others.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public final class ControllerContext {

	// static stuff

	private static final ThreadLocal<ControllerContext> threadContexts = new ThreadLocal<ControllerContext>();

	protected static ControllerContext getContext() {
		return threadContexts.get();
	}

	protected static void setContext(HttpServletRequest request, ControllerDescriptor controllerDesc) {
		threadContexts.set(new ControllerContext(request, controllerDesc));
	}

	protected static void clearContext() {
		threadContexts.remove();
	}

	// internal
	private ControllerResponse controllerResp;
	private Set<Cookie> cookies;
	private Map<String, String> headers;
	private Map<String, Object> output;
	private HttpServletRequest request;
	private ControllerDescriptor controllerDesc;
	private Locale locale;
	private ControllerDescriptor forward = null;

	private ControllerContext(HttpServletRequest request, ControllerDescriptor controllerDesc) {
		this.request = request;
		this.controllerDesc = controllerDesc;
	}

	// internal methods

	/**
	 * Gets the Servlet Context
	 */
	protected HttpServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Sets the controller response for this context.
	 */
	protected void setControllerResponse(ControllerResponse response) {
		if (!this.isAnswered() && !this.forwarded()) {
			this.controllerResp = response;
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}

	/**
	 * Mark this controller/context to be forwarded to the given {@link ControllerDescriptor}
	 * 
	 * @param forwardDescriptor
	 *            the {@link ControllerDescriptor} which this controller will be forwarded
	 */
	protected void forward(ControllerDescriptor forwardDescriptor) {
		if (!this.isAnswered() && !this.forwarded()) {
			this.forward = forwardDescriptor;
		} else {
			throw new IllegalStateException("Response already defined");
		}
	}

	/**
	 * Gets the forward {@link ControllerDescriptor}
	 * 
	 * @return the {@link ControllerDescriptor} to forward to
	 */
	protected ControllerDescriptor forward() {
		this.controllerDesc = this.forward;
		this.forward = null;
		return this.controllerDesc;
	}

	/**
	 * @param locale
	 *            the locale to be used to render response.
	 */
	protected void setLocale(Locale locale) {
		this.locale = locale;
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
	protected void addCookie(String name, String value) {
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
	protected void addCookie(String name, String value, String domain) {
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
	protected void addCookie(String name, String value, int maxAge) {
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
	protected void addCookie(String name, String value, String domain, int maxAge) {
		Cookie c = new Cookie(name, value);
		if (domain != null) {
			c.setDomain(domain);
		}
		if (maxAge != -1) {
			c.setMaxAge(maxAge);
		}
		this.getCookies().add(c);
	}

	/**
	 * Adds a header.
	 * 
	 * @param name
	 *            the name of the header
	 * @param value
	 *            the additional header value
	 */
	protected void addHeader(String name, String value) {
		this.getHeaders().put(name, value);
	}

	/**
	 * Adds an object to the output. This objects will be used to by the view to render output.
	 * 
	 * @param key
	 *            the output's key
	 * @param value
	 *            the output's value
	 */
	protected void addOutput(String key, Object value) {
		this.getOutput().put(key, value);
	}

	/**
	 * @return the controllerDescriptor
	 */
	public ControllerDescriptor getControllerDescriptor() {
		return this.controllerDesc;
	}

	/**
	 * @return the response's headers
	 */
	public Map<String, String> getHeaders() {
		if (this.headers == null) {
			this.headers = new HashMap<String, String>();
		}
		return this.headers;
	}

	/**
	 * @return response's cookies
	 */
	public Set<Cookie> getCookies() {
		if (this.cookies == null) {
			this.cookies = new HashSet<Cookie>();
		}
		return this.cookies;
	}

	/**
	 * @return response's output
	 */
	public Map<String, Object> getOutput() {
		if (this.output == null) {
			this.output = new HashMap<String, Object>();
		}
		return this.output;
	}

	/**
	 * @return <code>true</code> if request is already answered, <code>false</code> if not.
	 */
	public boolean isAnswered() {
		return this.controllerResp != null;
	}

	/**
	 * @return the {@link ControllerResponse}, or <code>null</code> if request not answered.
	 */
	public ControllerResponse getControllerResponse() {
		return this.controllerResp;
	}

	/**
	 * @return the locale to be used to render response.
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * Checks if this controller was forwarded
	 * 
	 * @return <code>true</code> if it was, <code>false</code> if not
	 */
	public boolean forwarded() {
		return forward != null;
	}

}
