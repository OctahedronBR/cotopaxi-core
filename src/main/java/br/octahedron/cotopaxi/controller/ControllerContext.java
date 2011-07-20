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

	protected static void setContext(HttpServletRequest request, String controllerName) {
		threadContexts.set(new ControllerContext(request, controllerName));
	}

	protected static void clearContext() {
		threadContexts.remove();
	}

	// internal
	private ControllerResponse controllerResp;
	private Locale locale;
	private Map<String, String> cookies;
	private Map<String, String> headers;
	private Map<String, Object> output;
	private HttpServletRequest request;
	private String controllerName;

	private ControllerContext(HttpServletRequest request, String controllerName) {
		this.request = request;
		this.locale = request.getLocale();
		this.controllerName = controllerName;
	}

	// internal methods

	protected HttpServletRequest getRequest() {
		return this.request;
	}
	
	protected void setControllerResponse(ControllerResponse response) {
		this.controllerResp = response;
	}

	public String getControllerName() {
		return controllerName;
	}

	public Map<String, String> getHeaders() {
		if (this.headers == null) {
			this.headers = new HashMap<String, String>();
		}
		return this.headers;
	}

	public Map<String, String> getCookies() {
		if (this.cookies == null) {
			this.cookies = new HashMap<String, String>();
		}
		return this.cookies;
	}

	public Map<String, Object> getOutput() {
		if (this.output == null) {
			this.output = new HashMap<String, Object>();
		}
		return this.output;
	}

	public boolean isAnswered() {
		return this.controllerResp != null;
	}
	
	public ControllerResponse getControllerResponse() {
		return this.controllerResp;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	
}
