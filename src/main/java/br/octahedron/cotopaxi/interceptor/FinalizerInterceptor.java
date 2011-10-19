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
package br.octahedron.cotopaxi.interceptor;

import br.octahedron.cotopaxi.controller.ControllerResponse;

/**
 * {@link FinalizerInterceptor} runs after the {@link ControllerResponse} be dispatched. This kind
 * of interceptors are useful to clean-housing stuff, such as close database connections, free
 * resources and etc.
 * 
 * This interceptors are globally and are executed for every Request.
 * 
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface FinalizerInterceptor {

	/**
	 * This method is called after the {@link ResponseDispatcher} renders the response to the
	 * client. It should be used to clean the house.
	 */
	public abstract void finish();
}
