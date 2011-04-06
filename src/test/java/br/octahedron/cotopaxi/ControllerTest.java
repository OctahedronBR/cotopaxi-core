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
package br.octahedron.cotopaxi;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.controller.FacadeOne;
import br.octahedron.cotopaxi.controller.FacadeThree;
import br.octahedron.cotopaxi.controller.FacadeTwo;
import br.octahedron.cotopaxi.controller.ModelController;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.inject.InstanceHandler;
import br.octahedron.cotopaxi.metadata.MetadataMapper;
import br.octahedron.cotopaxi.metadata.PageNotFoundExeption;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;
import br.octahedron.cotopaxi.model.attribute.converter.DateConverter;
import br.octahedron.cotopaxi.model.response.ActionResponse;
import br.octahedron.cotopaxi.model.response.ExceptionActionResponse;
import br.octahedron.cotopaxi.model.response.InvalidActionResponse;
import br.octahedron.cotopaxi.model.response.SuccessActionResponse;
import br.octahedron.cotopaxi.model.response.ActionResponse.Result;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ControllerTest {

	private ModelController controller;
	private MetadataMapper mapper;

	@Before
	public void setUp() throws SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
		CotopaxiConfigView configView = InstanceHandler.getInstance(CotopaxiConfigView.class);
		CotopaxiConfig config = configView.getCotopaxiConfig();
		config.addModelFacade(FacadeOne.class, FacadeTwo.class, FacadeThree.class);
		this.mapper = new MetadataMapper(configView);
		this.controller = new ModelController();
		DateConverter.setDateFormat("dd/MM/yyyy");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest1() throws FilterException, PageNotFoundExeption, IllegalArgumentException, IllegalAccessException {
		/*
		 * This test checks a model method that throws an exception
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/except").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.EXCEPTION, resp.getResult());
		assertEquals(Exception.class, ((ExceptionActionResponse) resp).getCause().getClass());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest2() throws FilterException, PageNotFoundExeption, IllegalArgumentException, IllegalAccessException {
		/*
		 * This test checks a success model execution
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/helloworld").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn("html");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals("Hello, World", ((SuccessActionResponse) resp).getReturnValue());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest3() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks a success model execution with parameter
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/hello/test").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		request.setRequestParameter("name", "test");
		expect(request.getRequestParameter("name")).andReturn("test");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals("Hello, test", ((SuccessActionResponse) resp).getReturnValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest4() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks a model execution with invalid parameter
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/hello/coder").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		request.setRequestParameter("name", "coder");
		expect(request.getRequestParameter("name")).andReturn("coder");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("name", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest5() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks a conversion error / conversion error is handled as an invalid attribute
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/int").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("int")).andReturn("lalala");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("int", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest6() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks invalid attribute - outside range
		 */
		// Prepare test
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/int").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("int")).andReturn("11");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("int", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest7() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks string array atts
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/varargs").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("str")).andReturn("name1,name2,name3,name4");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals(4, ((String[]) ((SuccessActionResponse) resp).getReturnValue()).length);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void controllerTest8() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks non given parameter
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/int").atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getRequestParameter("int")).andReturn(null);
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("int", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());
	}
}
