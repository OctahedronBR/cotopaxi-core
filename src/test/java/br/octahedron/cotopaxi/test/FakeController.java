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
package br.octahedron.cotopaxi.test;

import br.octahedron.cotopaxi.controller.Controller;

/**
 * Controller for tests
 * 
 * @author Danilo Queiroz - dpenna.queiroz
 */
public class FakeController extends Controller {

	public void getTest() {
		echo();
		out("msg", "hello " + in("name"));
		out("user", currentUser());
		if (this.subDomain().equals("www")) {
			success("get.vm");
		} else {
			redirect("/mimi");
		}
	}

	public void postTest() throws Exception {
		if (this.isAuthorized()) {
			out("msg", "hello noob");
			out("user", currentUser());
			session("something", "hey");
			jsonSuccess();
		} else {
			throw new Exception("User not authorized");
		}
	}
}
