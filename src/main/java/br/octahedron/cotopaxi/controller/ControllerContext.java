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
import java.util.Locale;
import java.util.Map;

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
	private Map<String, String> cookies;
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
		}
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
	public Map<String, String> getCookies() {
		if (this.cookies == null) {
			this.cookies = new HashMap<String, String>();
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
	 * @param locale
	 *            the locale to be used to render response.
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Checks if this controller was forwarded
	 * 
	 * @return <code>true</code> if it was, <code>false</code> if not
	 */
	public boolean forwarded() {
		return forward != null;
	}

	/**
	 * Mark this controller/context to be forwarded to the given {@link ControllerDescriptor}
	 * 
	 * @param forwardDescriptor
	 *            the {@link ControllerDescriptor} which this controller will be forwarded
	 */
	public void forward(ControllerDescriptor forwardDescriptor) {
		if (!this.isAnswered() && !this.forwarded()) {
			this.forward = forwardDescriptor;
		}
	}

	/**
	 * Gets the forward {@link ControllerDescriptor}
	 * 
	 * @return the {@link ControllerDescriptor} to forward to
	 */
	public ControllerDescriptor forward() {
		this.controllerDesc = this.forward;
		this.forward = null;
		return this.controllerDesc;
	}
}
