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
package br.octahedron.cotopaxi.model.auth;

import static br.octahedron.cotopaxi.model.auth.UserInfo.USER_INFO_ATTRIBUTE;
import br.octahedron.util.ThreadProperties;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public abstract class AbstractUserService {

	public final UserInfo getCurrentUser() {
		return (UserInfo) ThreadProperties.getProperty(USER_INFO_ATTRIBUTE);
	}

	protected final void loggedIn(UserInfo user) {
		ThreadProperties.setProperty(USER_INFO_ATTRIBUTE, user);
	}

	protected final void loggedOut() {
		ThreadProperties.setProperty(USER_INFO_ATTRIBUTE, null);
	}

}
