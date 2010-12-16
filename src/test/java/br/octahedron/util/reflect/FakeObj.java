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
package br.octahedron.util.reflect;

import javax.jdo.annotations.PrimaryKey;

import br.octahedron.cotopaxi.CotopaxiConfig;
import br.octahedron.cotopaxi.CotopaxiConfigurator;
import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.model.InputAdapter;

public class FakeObj implements CotopaxiConfigurator {

	@PrimaryKey
	private String key = "key";

	public String getKey() {
		return this.key;
	}

	@Action(adapter = InputAdapter.class, url = "/{mimi}")
	public boolean test() {
		return true;
	}

	@Override
	public void configure(CotopaxiConfig configure) {

	}
}
