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

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired.LoginRequiredMetadata;

/**
 * This entity is responsible by check the permissions of an user to access some resource.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class AuthManager {

	@Inject
	private UserLookupStrategy userLookupStrategy;

	/**
	 * @param userLookupStrategy Sets the {@link UserLookupStrategy}
	 */
	public void setUserLookupStrategy(UserLookupStrategy userLookupStrategy) {
		this.userLookupStrategy = userLookupStrategy;
	}

	/**
	 * Checks if the current user satisfy the given login requirements.
	 * 
	 * The login requirements are provided using the {@link LoginRequired} annotation.
	 * 
	 * The current user is recovered using the configured {@link UserLookupStrategy}.
	 * 
	 * @see UserLookupStrategy
	 */
	public void authorizeUser(RequestWrapper request, LoginRequiredMetadata loginMetadata) throws UserNotLoggedException, UserNotAuthorizedException {
		UserInfo user = this.userLookupStrategy.getCurrentUser(request);
		if (user != null) {
			String role = loginMetadata.getRequiredRole();
			if (!user.satisfyRole(role)) {
				throw new UserNotAuthorizedException("User " + user.getUsername() + " doesn't satify role " + role);
			}
		} else {
			throw new UserNotLoggedException("There's no user logged!", this.userLookupStrategy.getLoginURL(request.getURL()));
		}
	}
}
