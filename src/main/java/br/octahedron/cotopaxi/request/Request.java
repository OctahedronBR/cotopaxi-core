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
package br.octahedron.cotopaxi.request;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import br.octahedron.cotopaxi.HTTPMethod;
import br.octahedron.cotopaxi.controller.Controller;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface Request {
	
	/**
	 * @return
	 */
	public abstract HTTPMethod getMethod();

	// TODO remove set methods from here
	/**
	 * @param instance
	 */
	public abstract void setController(Controller instance);

	/**
	 * @param attName
	 * @param value
	 */
	public abstract void setRequestParameter(String key, String value);

	/**
	 * @return
	 */
	public abstract String getURL();

	/**
	 * @return
	 */
	public abstract Controller getController();

	/**
	 * @return The parameter's value for the given parameter's name, if exists, or null if doesn't exists a parameter with the given name.
	 */
	public abstract String getRequestParameter(String parameterName);

	/**
	 * @return
	 */
	public abstract HttpSession getSession();

	/**
	 * @param string
	 * @return
	 */
	public abstract String getHeaderParameter(String string);

	/**
	 * @return
	 */
	public abstract Collection<Cookie> getCookies();

	
}
