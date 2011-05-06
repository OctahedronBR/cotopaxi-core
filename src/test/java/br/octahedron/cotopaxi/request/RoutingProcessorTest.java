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
package br.octahedron.cotopaxi.request;

import static br.octahedron.cotopaxi.HTTPMethod.GET;
import static br.octahedron.cotopaxi.HTTPMethod.POST;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.HTTPMethod;
import br.octahedron.cotopaxi.inject.InstanceHandler;
import br.octahedron.cotopaxi.request.Controllers.ControllerFive;
import br.octahedron.cotopaxi.request.Controllers.ControllerFour;
import br.octahedron.cotopaxi.request.Controllers.ControllerOne;
import br.octahedron.cotopaxi.request.Controllers.ControllerSix;
import br.octahedron.cotopaxi.request.Controllers.ControllerThree;
import br.octahedron.cotopaxi.request.Controllers.ControllerTwo;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RoutingProcessorTest {
	
	private RoutingProcessor router = new RoutingProcessor();
	private Request request = createMock(Request.class);
	
	@Before
	public void setUp() {
		router.addRoute("/test", HTTPMethod.GET, ControllerOne.class);
		router.addRoute("/test", HTTPMethod.POST, ControllerTwo.class);
		router.addRoute("/{username}", HTTPMethod.GET, ControllerThree.class);
		router.addRoute("/{username}/{id}", HTTPMethod.GET, ControllerFour.class);
		router.addRoute("/{username}/edit", HTTPMethod.POST, ControllerFive.class);
		router.addRoute("/{username}/edit/{id}", HTTPMethod.POST, ControllerSix.class);
	}
	
	@Test(expected = NotFoundExeption.class)
	public void testNotFound() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/trollface");
		expect(request.getMethod()).andReturn(POST);
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testStaticGet() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/test");
		expect(request.getMethod()).andReturn(GET);
		request.setController(InstanceHandler.getInstance(ControllerOne.class));		
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testStaticPost() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/test");
		expect(request.getMethod()).andReturn(POST);
		request.setController(InstanceHandler.getInstance(ControllerTwo.class));		
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic1() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/trollface").anyTimes();
		expect(request.getMethod()).andReturn(GET);
		request.setController(InstanceHandler.getInstance(ControllerThree.class));
		request.setRequestParameter("username", "trollface");
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic2() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/trollface/1").anyTimes();
		expect(request.getMethod()).andReturn(GET);
		request.setController(InstanceHandler.getInstance(ControllerFour.class));
		request.setRequestParameter("username", "trollface");
		request.setRequestParameter("id", "1");
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic3() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/trollface/edit").anyTimes();
		expect(request.getMethod()).andReturn(POST);
		request.setController(InstanceHandler.getInstance(ControllerFive.class));
		request.setRequestParameter("username", "trollface");
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	@Test
	public void testDynamic4() throws RequestProcessingException, InstantiationException {
		// setup mock
		expect(request.getURL()).andReturn("/trollface/edit/1").anyTimes();
		expect(request.getMethod()).andReturn(POST);
		request.setController(InstanceHandler.getInstance(ControllerSix.class));
		request.setRequestParameter("username", "trollface");
		request.setRequestParameter("id", "1");
		// test
		replay(request);
		try {
			router.process(request);
		} finally {
			verify(request);
		}
	}
	
	
}
