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

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.view.ResponseDispatcher;
import br.octahedron.cotopaxi.view.response.WriteableResponse;

/**
 * An interceptor that intercepts the {@link ResponseDispatcher} processing. It intercepts the
 * processing in two different moments: just before the render, making possible decorate the writer
 * to be used to render the response; and after render the response, making possible keep control of
 * any needed resource, such as database connections, and others.
 * 
 * This interceptors are globally and are executed for every {@link WriteableResponse}. 
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class ResponseDispatcherInterceptor {

	/**
	 * Gets an {@link Writer} to be used by the {@link ResponseDispatcher} to write response to
	 * user.
	 * 
	 * It makes possible to decorate the given {@link Writer}.
	 * 
	 * @param writer
	 *            The {@link HttpServletResponse} writer.
	 * @return the new writer to be used to write the response (a decorator) or the given writer
	 *         itself.
	 */
	public Writer getWriter(Writer writer) {
		return writer;
	}

	/**
	 * This method is called after the {@link ResponseDispatcher} renders the response to the
	 * client. It should be used to clean the house.
	 */
	public void finish() {
		// do nothing
	}
}
