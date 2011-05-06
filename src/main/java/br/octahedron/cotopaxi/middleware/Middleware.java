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
package br.octahedron.cotopaxi.middleware;

import br.octahedron.cotopaxi.request.Request;
import br.octahedron.cotopaxi.response.Response;

/**
 * Defines a interface for middleware . Middlewares are a kind of interceptors that act during the
 * {@link Request} processing. They can act only on specific {@link RequestState}.
 * 
 * TODO improve it!
 * 
 * @see RequestState
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class Middleware {

	public void processPreRoute(Request request) {
		// nothing to do! Just override if necessary
	}
	
	public void processPreExcecution(Request request) {
		// nothing to do! Just override if necessary
	}
	
	public void processPreRender(Response response) {
		// nothing to do! Just override if necessary
	}

	public void processPreDeliver(Response response) {
		// nothing to do! Just override if necessary
	}
	
}
