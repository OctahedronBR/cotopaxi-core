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

import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;

/**
 * Thrown to indicate that the execution flow was interrupted by the Filter. It should be used, for
 * example, by the capability filter when it detects that the {@link DatastoreFacade} is on
 * read-only mode.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class FilterException extends Exception {

	private static final long serialVersionUID = -3194948115528012343L;

	public FilterException(String message) {
		super(message);
	}

	public FilterException(String message, Throwable cause) {
		super(message, cause);
	}
}