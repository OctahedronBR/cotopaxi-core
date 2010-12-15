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

import br.octahedron.cotopaxi.controller.adapters.NameAdapter;
import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.metadata.annotation.Response;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class FacadeOne {

	@Action(url = "/except", method = HTTPMethod.GET)
	public void throwException() throws Exception {
		throw new Exception("test");
	}

	@Action(url = "/helloworld")
	public String helloWorld() {
		return "Hello, World";
	}

	@Action(url = "/hello/{name}", method = HTTPMethod.POST, adapter = NameAdapter.class)
	@Response(returnName = "message")
	public String helloSomeone(String name) {
		return "Hello, " + name;
	}
}
