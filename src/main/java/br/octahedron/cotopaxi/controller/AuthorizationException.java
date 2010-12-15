package br.octahedron.cotopaxi.controller;

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

/**
 * Indicates that an an request cannot be answered due authorization requirements.
 * 
 * It can happen if the request needs an logged user, and there's no user logged at current session,
 * or if the logged user doesn't has the required role.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class AuthorizationException extends Exception {

	private static final long serialVersionUID = -4509198861899259133L;
	private String redirectURL;

	public AuthorizationException(String message, String redirectURL) {
		super(message);
		this.redirectURL = redirectURL;
	}

	public String getRedirectURL() {
		return this.redirectURL;
	}
}
