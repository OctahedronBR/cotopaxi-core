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
package br.octahedron.cotopaxi.controller.auth;

import br.octahedron.cotopaxi.InputHandler;
import br.octahedron.cotopaxi.controller.RedirectStrategy;
import br.octahedron.cotopaxi.model.response.ActionResponse;
import br.octahedron.cotopaxi.model.response.ActionResponse.Result;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class SuccessfulLoginRedirectStrategy implements RedirectStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.controller.RedirectStrategy#getRedirectURL(br.octahedron.cotopaxi.
	 * model.response.ActionResponse, br.octahedron.cotopaxi.InputHandler)
	 */
	@Override
	public String getRedirectURL(ActionResponse response, InputHandler inputHandler) {
		return inputHandler.getRequestParameter(SessionUserLookupStrategy.DEFAULT_REDIRECT_ATTRIBUTE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.controller.RedirectStrategy#shouldRedirect(br.octahedron.cotopaxi.
	 * model.response.ActionResponse)
	 */
	@Override
	public boolean shouldRedirect(ActionResponse response) {
		return response.getResult() == Result.SUCCESS;
	}

}
