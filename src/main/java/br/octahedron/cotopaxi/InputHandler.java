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

import java.util.Collection;

/**
 * This entity handles the inupt parameters from servlet. It provides access to the Servlet Request
 * Parameters and Servlet Session Parameters.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface InputHandler {

	/**
	 * Gets an parameter's value from request.
	 * 
	 * @param name
	 *            the parameter's name
	 * @return the parameter's value, or null if doesn't exist such parameter
	 */
	public String getRequestParameter(String name);

	/**
	 * Gets an parameter's value from session.
	 * 
	 * @param name
	 *            the parameter's name
	 * @return the parameter's value, or null if doesn't exist such parameter
	 */
	public Object getSessionAttribute(String name);

	/**
	 * Gets a collection with all session attributes' names.
	 * 
	 * @return a collection with all session attributes' names.
	 */
	public Collection<String> getSessionAttributes();

}
