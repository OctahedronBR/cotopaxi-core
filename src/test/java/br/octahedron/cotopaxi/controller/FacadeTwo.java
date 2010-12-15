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

import java.util.Date;

import br.octahedron.cotopaxi.controller.adapters.IntAdapter;
import br.octahedron.cotopaxi.controller.adapters.ManyAdapter;
import br.octahedron.cotopaxi.controller.adapters.VarargsAdapter;
import br.octahedron.cotopaxi.metadata.annotation.Action;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class FacadeTwo {

	@Action(adapter = ManyAdapter.class, url = "/two/many", filters = { MyFilter.class })
	public void receiveManyParameters(String[] str, Date when, int i) {

	}

	@Action(adapter = VarargsAdapter.class, url = "/two/varargs")
	public String[] strArgs(String... args) {
		return args;
	}

	@Action(adapter = IntAdapter.class, url = "/two/int")
	public void integer(int number) {

	}
}
