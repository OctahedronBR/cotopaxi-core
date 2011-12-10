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
package br.octahedron.cotopaxi.route;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.controller.ControllerDescriptor;
import br.octahedron.cotopaxi.route.NotFoundExeption;
import br.octahedron.cotopaxi.route.Router;


/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RoutingProcessorTest {
	
	private Router router = new Router();
	private HttpServletRequest request = createMock(HttpServletRequest.class);
	
	@Before
	public void setUp() {
		router.addRoute(new ControllerDescriptor("/test", "get", "Test1", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/test", "post", "Test2", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/{username}", "get", "ShowUser", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/{username}/edit","post", "EditUser", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/{username}/{id}", "get", "UserPost1", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/{username}/edit/{id}","post","UserPost2", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/{username}/email/{email}", "post", "UserPost3", "java.lang.String"));
		router.addRoute(new ControllerDescriptor("/users/search/{name}", "get", "SearchUser", "java.lang.String"));
	}
	
	@Test(expected = NotFoundExeption.class)
	public void testNotFound() throws NotFoundExeption {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface");
		expect(request.getMethod()).andReturn("POST");
		// test
		replay(request);
		try {
			router.route(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testStaticGet() throws NotFoundExeption  {
		// setup mock
		expect(request.getRequestURI()).andReturn("/test");
		expect(request.getMethod()).andReturn("GET");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("Test1", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testStaticPost() throws NotFoundExeption {
		// setup mock
		expect(request.getRequestURI()).andReturn("/test");
		expect(request.getMethod()).andReturn("POST");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("Test2", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic1() throws NotFoundExeption {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface").anyTimes();
		expect(request.getMethod()).andReturn("GET");
		request.setAttribute("username", "trollface");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("ShowUser", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic2() throws NotFoundExeption {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface/1").anyTimes();
		expect(request.getMethod()).andReturn("GET");
		request.setAttribute("username", "trollface");
		request.setAttribute("id", "1");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("UserPost1", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic3() throws NotFoundExeption {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface/edit").anyTimes();
		expect(request.getMethod()).andReturn("POST");
		request.setAttribute("username", "trollface");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("EditUser", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic4() throws NotFoundExeption  {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface/edit/1").anyTimes();
		expect(request.getMethod()).andReturn("POST");
		request.setAttribute("username", "trollface");
		request.setAttribute("id", "1");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("UserPost2", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic5() throws NotFoundExeption  {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface/email/test@example.com").anyTimes();
		expect(request.getMethod()).andReturn("POST");
		request.setAttribute("username", "trollface");
		request.setAttribute("email", "test@example.com");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("UserPost3", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	
	@Test
	public void testDynamic6() throws NotFoundExeption  {
		// setup mock
		expect(request.getRequestURI()).andReturn("/trollface/email/test-troll").anyTimes();
		expect(request.getMethod()).andReturn("POST");
		request.setAttribute("username", "trollface");
		request.setAttribute("email", "test-troll");
		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("UserPost3", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic7() throws NotFoundExeption  {
		// setup mock
		expect(request.getRequestURI()).andReturn("/users/search/vitor%20avelino").anyTimes();
		expect(request.getMethod()).andReturn("GET");
		request.setAttribute("name", "vitor%20avelino");

		// test
		replay(request);
		try {
			ControllerDescriptor desc = router.route(request); 
			assertEquals("SearchUser", desc.getControllerName());
		} finally {
			verify(request);
		}
	}
	
}
