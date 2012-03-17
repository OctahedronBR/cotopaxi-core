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

import static br.octahedron.cotopaxi.controller.InputController.USERNAME_KEY;
import static br.octahedron.cotopaxi.controller.InputController.USER_AUTHORIZED;
import static br.octahedron.cotopaxi.test.CotopaxiTestHelper.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * A Request representation to be used by the test mechanism. It provides methods to configure a
 * simple request, to simulate request from HTTP clients.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class Request {

	private String method;
	private String url;
	private String serverName = "localhost";
	private Map<String, ArrayList<String>> input = new HashMap<String, ArrayList<String>>();
	private Map<String, String> cookies = new HashMap<String, String>();
	private Map<String, String> headers = new HashMap<String, String>();

	/**
	 * Creates a new request.
	 * 
	 * Empty constructor.
	 */
	protected Request() {
	}

	/**
	 * Creates a new request for the given url, using the given http method.
	 * 
	 * @param url
	 *            The url being requested. Should be relative.
	 * @param method
	 *            The http method being simulated.
	 */
	protected Request(String url, String method) {
		this.url = url;
		this.method = method;
	}

	/**
	 * Sets the request url.
	 * 
	 * @param url
	 *            The url being requested. Should be relative.
	 * @param method
	 *            The http method being simulated.
	 * @return This class, to chain calls.
	 */
	public Request url(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @param method
	 *            The http method being simulated.
	 * @return This class, to chain calls.@return
	 */
	public Request method(String method) {
		this.method = method;
		return this;
	}

	/**
	 * The full server name. If you need to configure a subdomain for your controller, you should
	 * set the full server name including the subdomain. Eg.: subdomain.example.com
	 * 
	 * @param serverName
	 *            The full server name.
	 * 
	 * @return This class, to chain calls.
	 */
	public Request serverName(String serverName) {
		this.serverName = serverName;
		return this;
	}

	/**
	 * Sets an input attribute
	 * 
	 * @param key
	 *            The input key
	 * @param value
	 *            The input value
	 * @return This class, to chain calls.
	 */
	public Request in(String key, String value) {
		ArrayList<String> values;
		if (this.input.containsKey(key)) {
			values = this.input.get(key);
		} else {
			values = new ArrayList<String>();
		}
		values.add(value);
		this.input.put(key, values);
		return this;
	}

	/**
	 * Sets an input cookie value
	 * 
	 * @param key
	 *            The cookie name/key
	 * @param value
	 *            The cookie's value
	 * @return This class, to chain calls.
	 */
	public Request cookie(String key, String value) {
		this.cookies.put(key, value);
		return this;
	}

	/**
	 * Sets an input header value
	 * 
	 * @param key
	 *            The header's name/key
	 * @param value
	 *            The header's value
	 * @return This class, to chain calls.
	 */
	public Request header(String key, String value) {
		this.headers.put(key, value);
		return this;
	}

	/**
	 * Marks the request as authorized.
	 * 
	 * @return This class, to chain calls.
	 */
	public Request authorized() {
		this.in(USER_AUTHORIZED, "true");
		return this;
	}

	/**
	 * Sets the given user as the current user.
	 * 
	 * @param userID
	 *            The current user's ID
	 * @return This class, to chain calls.
	 */
	public Request currentUser(String userID) {
		session().setAttribute(USERNAME_KEY, userID);
		return this;
	}

	/**
	 * Gets this request as a {@link HttpServletRequest}. It creates a {@link Proxy} to handler the
	 * incomming method calls.
	 * 
	 * @return This request as a {@link HttpServletRequest}.
	 */
	protected HttpServletRequest servletRequest() {
		return (HttpServletRequest) Proxy.newProxyInstance(HttpServletRequest.class.getClassLoader(), new Class<?>[] { HttpServletRequest.class },
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
						String name = m.getName();
						if (name == "getServerName") {
							return serverName;
						} else if (name == "getRequestURL") {
							return "http://" + serverName + url;
						} else if (name == "getRequestURI") {
							return url;
						} else if (name == "getParameter" || name == "getAttribute") {
							String param = (String) args[0];
							ArrayList<String> values = input.get(param);
							return (values != null)?values.get(0):null;
						} else if (name == "getHeader") {
							String param = (String) args[0];
							return headers.get(param);
						} else if (name == "getParameterMap") {
							Map<String, String[]> result = new HashMap<String, String[]>();
							for(Entry<String, ArrayList<String>> entry : input.entrySet()) {
								int size = entry.getValue().size();
								result.put(entry.getKey(), entry.getValue().toArray(new String[size]));
							}
							return result;
						} else if (name == "getCookies") {
							ArrayList<Cookie> cks = new ArrayList<Cookie>();
							for (Entry<String, String> entry : cookies.entrySet()) {
								cks.add(new Cookie(entry.getKey(), entry.getValue()));
							}
							return cks.toArray(new Cookie[cks.size()]);
						} else if (name == "getSession") {
							return session();
						} else if (name == "setAttribute") {
							String key = (String) args[0];
							String value = (String) args[1];
							Request.this.in(key, value);
							return null;
						} else if (name == "getMethod") {
							return method;
						} else if (name == "getLocale") {
							return Locale.getDefault();
						} else {
							throw new UnsupportedOperationException(String.format("Method %s not supported", name));
						}
					}
				});
	}
}
