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

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.model.response.ActionResponse;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class MockFilter implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.controller.Filter#doAfter(br.octahedron.controller.RequestHandler,
	 * br.octahedron.controller.response.Response)
	 */
	@Override
	public void doAfter(RequestWrapper requestWrapper, ActionResponse response) throws FilterException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.controller.Filter#doBefore(br.octahedron.controller.RequestHandler)
	 */
	@Override
	public void doBefore(RequestWrapper requestHandler) throws FilterException {

	}

}
