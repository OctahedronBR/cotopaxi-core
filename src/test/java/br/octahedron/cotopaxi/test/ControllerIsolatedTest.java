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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.controller.ControllerDescriptor;
import br.octahedron.cotopaxi.route.NotFoundExeption;
import br.octahedron.cotopaxi.route.Router;

/**
 * Tests for Controller Test Framework
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class ControllerIsolatedTest extends CotopaxiTestHelper {

	private Router router = new Router();
	private ControllerDescriptor desc1;
	private ControllerDescriptor desc2;

	@Before
	public void routerSetup() {
		router.forceReset();
		desc1 = new ControllerDescriptor("/test", "get", "Test", "br.octahedron.cotopaxi.test.FakeController");
		desc2 = new ControllerDescriptor("/test/{something}", "post", "Test", "br.octahedron.cotopaxi.test.FakeController");
		router.addRoute(desc1);
		router.addRoute(desc2);
	}

	@Test
	public void routerTest() throws NotFoundExeption {
		Request req = request("/test", "get");
		ControllerDescriptor desc = router.route(req.servletRequest());
		assertTrue(desc1 == desc);
		
		req = request("/test/lalala", "post");
		assertNull(req.servletRequest().getAttribute("something"));
		desc = router.route(req.servletRequest());
		assertTrue(desc2 == desc);
		assertEquals("lalala", req.servletRequest().getAttribute("something"));
	}

	@Test(expected=NotFoundExeption.class)
	public void routerFailTest() throws NotFoundExeption {
		Request req = request("/test", "post");
		router.route(req.servletRequest());
	}
}
