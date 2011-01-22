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

/**
 * It provides an strategy to recover the current logged user.
 * 
 * It's used by the authentication/authorization mechanism to loads the current user and check it
 * credentials.
 * 
 * 
 * 
 * @author Danilo Penna Queiroz- daniloqueiroz@octahedron.com.br
 */
public interface UserLookupStrategy {

	/**
	 * @return the current logged user, if exists, or <code>null</code> otherwise.
	 */
	public abstract UserInfo getCurrentUser(InputHandler input);

	/**
	 * 
	 * @param redirectURL
	 *            The URL which user will be redirect after login
	 * @return gets the login URL. This URL is used to redirect the request if user isn't logged.
	 */
	public abstract String getLoginURL(String redirectURL);
}
