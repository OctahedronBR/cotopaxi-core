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

import br.octahedron.cotopaxi.CotopaxiConfig;
import br.octahedron.cotopaxi.CotopaxiConfigurator;
import br.octahedron.cotopaxi.controller.adapters.MappingBoth;
import br.octahedron.cotopaxi.controller.adapters.MappingGet;
import br.octahedron.cotopaxi.controller.adapters.MappingGetAtts;
import br.octahedron.cotopaxi.controller.adapters.MappingGetID;
import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;

/**
 * @author Danilo Penna Queiroz - daniloqueiro@octahedron.com.br
 */

public class FakeModelFacade implements CotopaxiConfigurator {

	@Action(adapter = MappingBoth.class, url = "/user/")
	public void doBoth() {
	}

	@Action(url = "/", method = HTTPMethod.GET)
	public void doGet() {

	}

	@Action(adapter = MappingGet.class, url = "/{name}", method = HTTPMethod.GET)
	public void doGet(String name) {

	}

	@Action(url = "/edit", method = HTTPMethod.POST)
	public void doPost() {
	}

	@Action(adapter = MappingGetID.class, url = "/user/{id}", method = HTTPMethod.GET)
	public int getId(int id) {
		return id;
	}

	@Action(adapter = MappingGetAtts.class, url = "/user/{name}/{id}/view")
	public int viewUserId(String name, int id) {
		return id;
	}

	@Override
	public void configure(CotopaxiConfig configure) {

	}

}
