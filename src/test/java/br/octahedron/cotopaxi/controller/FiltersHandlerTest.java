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

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.util.reflect.InstanceHandler;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class FiltersHandlerTest {

	private InstanceHandler<Filter> filtersHandler;

	@Before
	public void setUp() {
		this.filtersHandler = new InstanceHandler<Filter>();
	}

	@Test
	public void getFilterTest() {
		Filter f1 = this.filtersHandler.getInstance(MockFilter.class);
		Filter f2 = this.filtersHandler.getInstance(MockFilter.class);
		assertSame(f1, f2);
		assertSame(f1, this.filtersHandler.getInstance(MockFilter.class));
	}
}
