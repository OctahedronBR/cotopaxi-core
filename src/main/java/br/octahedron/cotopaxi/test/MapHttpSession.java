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

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * A very simple {@link HttpSession} implementation using a {@link Map}.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
@SuppressWarnings("deprecation")
class MapHttpSession implements HttpSession {

	private Map<String, Object> session;

	/**
	 * @param session
	 */
	public MapHttpSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public long getCreationTime() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public String getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public long getLastAccessedTime() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public ServletContext getServletContext() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void setMaxInactiveInterval(int interval) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public int getMaxInactiveInterval() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Enumeration getAttributeNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @throws UnsupportedOperationException
	 */
	@Override
	public String[] getValueNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getAttribute(String name) {
		return this.session.get(name);
	}

	@Override
	public Object getValue(String name) {
		return this.getAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		this.session.put(name, value);
	}

	@Override
	public void putValue(String name, Object value) {
		this.setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		this.session.remove(name);
	}

	@Override
	public void removeValue(String name) {
		this.removeAttribute(name);

	}

	@Override
	public void invalidate() {
		this.session.clear();
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
