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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates the user's informations used by the Login mechanism
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class UserInfo implements Serializable {

	public static final String USERNAME_ATTRIBUTE_NAME = "USERNAME";
	public static final String USER_INFO_ATTRIBUTE = "USER_INFO";

	private static final long serialVersionUID = -2985274865291979315L;
	private String username;
	private Set<String> roles;

	public UserInfo(String username, Set<String> roles) {
		this.username = username;
		this.roles = new HashSet<String>(roles);
	}

	public UserInfo(String username, String... roles) {
		this.username = username;
		this.roles = new HashSet<String>(roles.length);

		for (String role : roles) {
			this.roles.add(role);
		}
	}

	public String getUsername() {
		return this.username;
	}

	public Set<String> getRoles() {
		return this.roles;
	}

	public boolean satisfyRole(String role) {
		// all users satisfy empty role
		return role.isEmpty() || this.roles.contains(role);
	}
}
