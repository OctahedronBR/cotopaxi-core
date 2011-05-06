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

import br.octahedron.cotopaxi.request.Request;
import br.octahedron.cotopaxi.response.Response;
import br.octahedron.cotopaxi.response.ResponseProcessor;

/**
 * Controllers interface
 * 
 * @author Danilo Penna Queiroz - dpenna.queiroz@octahedron.com.br
 */
public interface Controller {

	/**
	 * Executes the {@link Controller} processing the given {@link Request} and delivering the {@link Response} to the given {@link ResponseProcessor}
	 * @param request
	 * @param responseProcessor
	 */
	public abstract void process(Request request, ResponseProcessor responseProcessor);
	
	public Object getMiddlewareConfigs();
}
