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

import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;

/**
 * @author Danilo Penna Queiroz - email@octahedron.com.br
 * 
 */
public class FacadeThree {

	@LoginRequired
	@Action(url = "/restricted1", method = HTTPMethod.GET)
	public void doNothing() {
		// do nothing
	}

	@LoginRequired
	@Action(url = "/restricted2", method = HTTPMethod.GET)
	public void doNothing2() {
		// do nothing
	}

	@LoginRequired(requiredRole = "admin")
	@Action(url = "/restricted3", method = HTTPMethod.GET)
	public void doNothing3() {
		// do nothing
	}

	@Action(url = "/three/user", method = HTTPMethod.GET)
	public void logout(String username, String something) {

	}
}
