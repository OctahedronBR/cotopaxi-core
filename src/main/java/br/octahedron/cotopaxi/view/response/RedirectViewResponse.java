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
package br.octahedron.cotopaxi.view.response;

import java.io.IOException;

import br.octahedron.cotopaxi.ResponseWrapper;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RedirectViewResponse implements ViewResponse {
	
	private String redirectURL;

	protected RedirectViewResponse(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.view.ViewResponse#dispatch(br.octahedron.cotopaxi.ResponseWrapper)
	 */
	@Override
	public void dispatch(ResponseWrapper response) throws IOException {
		response.redirect(this.redirectURL);
	}

}
