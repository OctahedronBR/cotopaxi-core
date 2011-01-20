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

import br.octahedron.cotopaxi.InputHandler;
import br.octahedron.cotopaxi.model.response.ActionResponse;
import br.octahedron.cotopaxi.view.response.ViewResponse;

/**
 * A strategy to decide if a response should be a redirect or not.
 * 
 * When creating the {@link ViewResponse}, it checks first if it should redirect response and, if it
 * should, asks the strategy fo the redirect url.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface RedirectStrategy {

	/**
	 * Decides if should redirect response, based on the given {@link ActionResponse}
	 * @param response the {@link ActionResponse}
	 * @return <code>true</code> if it should, <code>false</code> otherwise.
	 */
	public boolean shouldRedirect(ActionResponse response);

	/**
	 * The URL to response be redirect.
	 * @param response the {@link ActionResponse}
	 * @param inputAdapter 
	 * @return the URL to redirect response
	 */
	public String getRedirectURL(ActionResponse response, InputHandler inputHandler);
}
