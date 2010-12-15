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
package br.octahedron.cotopaxi.controller.filter;

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.model.ActionResponse;

/**
 * A Request filter. It specify methods to be executed before or after the model execution. It can
 * be used to modify input values or to clear the house after model execution, for example.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface Filter {

	/**
	 * Executed before the model be executed. Filters can perform operations using or over the
	 * request parameters.
	 * 
	 * @param requestWrapper
	 *            the request handler.
	 */
	public void doBefore(RequestWrapper requestWrapper) throws FilterException;

	/**
	 * Executed after the model be executed, but before the response be processed.
	 * 
	 * @param requestWrapper
	 * @param response
	 */
	public void doAfter(RequestWrapper requestWrapper, ActionResponse response) throws FilterException;

}
