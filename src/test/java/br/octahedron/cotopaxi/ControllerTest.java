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
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.config.CotopaxiConfig;
import br.octahedron.cotopaxi.config.CotopaxiConfigView;
import br.octahedron.cotopaxi.config.CotopaxiConfigViewImpl;
import br.octahedron.cotopaxi.controller.AuthorizationException;
import br.octahedron.cotopaxi.controller.ControllerManager;
import br.octahedron.cotopaxi.controller.FiltersHelper;
import br.octahedron.cotopaxi.controller.MyExceptionFilterAfter;
import br.octahedron.cotopaxi.controller.MyExceptionFilterBefore;
import br.octahedron.cotopaxi.controller.MyFilter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.metadata.MetatadaMapper;
import br.octahedron.cotopaxi.metadata.PageNotFoundExeption;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;
import br.octahedron.cotopaxi.model.ActionResponse;
import br.octahedron.cotopaxi.model.ExceptionActionResponse;
import br.octahedron.cotopaxi.model.InvalidActionResponse;
import br.octahedron.cotopaxi.model.SuccessActionResponse;
import br.octahedron.cotopaxi.model.ActionResponse.Result;
import br.octahedron.cotopaxi.model.attribute.converter.DateConverter;
import br.octahedron.cotopaxi.model.auth.UserInfo;
import br.octahedron.util.ThreadProperties;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ControllerTest {

	private static final String[] FACADES = { "br.octahedron.cotopaxi.controller.FacadeOne", "br.octahedron.cotopaxi.controller.FacadeTwo",
			"br.octahedron.cotopaxi.controller.FacadeThree" };
	private ControllerManager controller;
	private MetatadaMapper mapper;

	@Before
	public void setUp() throws SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
		FiltersHelper.reset();
		this.mapper = new MetatadaMapper(FACADES);
		CotopaxiConfigView.Handler.reset();
		this.controller = new ControllerManager(CotopaxiConfigView.Handler.getConfigView());
		DateConverter.setDateFormat("dd/MM/yyyy");
	}

	@Test
	public void controllerTest1() throws AuthorizationException, FilterException, PageNotFoundExeption, IllegalArgumentException,
			IllegalAccessException {
		/*
		 * This test checks a model method that throws an exception
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/except").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.EXCEPTION, resp.getResult());
		assertEquals(Exception.class, ((ExceptionActionResponse) resp).getCause().getClass());
	}

	@Test
	public void controllerTest2() throws AuthorizationException, FilterException, PageNotFoundExeption, IllegalArgumentException,
			IllegalAccessException {
		/*
		 * This test checks a success model execution
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/helloworld").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn("html");
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals("Hello, World", ((SuccessActionResponse) resp).getReturnValue());

	}

	@Test
	public void controllerTest3() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
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
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals("Hello, test", ((SuccessActionResponse) resp).getReturnValue());
	}

	@Test
	public void controllerTest4() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
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
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("name", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());

	}

	@Test
	public void controllerTest5() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks a conversion error / conversion error is handled as an invalid attribute
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/int").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("int")).andReturn("lalala");
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("int", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());
	}

	@Test
	public void controllerTest6() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
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
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("int", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());
	}

	@Test
	public void controllerTest7() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks string array atts
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/varargs").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("str")).andReturn("name1,name2,name3,name4");
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals(4, ((String[]) ((SuccessActionResponse) resp).getReturnValue()).length);
	}

	@Test
	public void controllerTest8() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks many atts and local Filter
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/many").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("str")).andReturn("name1,name2,name3,name4");
		expect(request.getRequestParameter("when")).andReturn("06/11/2010");
		expect(request.getRequestParameter("int")).andReturn("5");
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals(1, FiltersHelper.getFilterBefore());
		assertEquals(1, FiltersHelper.getFilterAfter());
	}

	@Test
	public void controllerTest9() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test using Global Filter
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/varargs").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("str")).andReturn("name1,name2,name3,name4");
		replay(request);
		CotopaxiConfig config = ((CotopaxiConfigViewImpl) CotopaxiConfigView.Handler.getConfigView()).getControllerConfig();
		config.addGlobalFilter(MyFilter.class);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals(4, ((String[]) ((SuccessActionResponse) resp).getReturnValue()).length);
		assertEquals(1, FiltersHelper.getFilterBefore());
		assertEquals(1, FiltersHelper.getFilterAfter());
	}

	@Test
	public void controllerTest10() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks many atts with Filters (Global and Local)
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/many").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("str")).andReturn("name1,name2,name3,name4");
		expect(request.getRequestParameter("when")).andReturn("06/11/2010");
		expect(request.getRequestParameter("int")).andReturn("5");
		replay(request);
		CotopaxiConfig config = ((CotopaxiConfigViewImpl) CotopaxiConfigView.Handler.getConfigView()).getControllerConfig();
		config.addGlobalFilter(MyFilter.class);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
		assertEquals(2, FiltersHelper.getFilterBefore());
		assertEquals(2, FiltersHelper.getFilterAfter());
	}

	@Test(expected = FilterException.class)
	public void controllerTest11() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks many atts with Filters (Global and Local)
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/many").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);
		CotopaxiConfig config = ((CotopaxiConfigViewImpl) CotopaxiConfigView.Handler.getConfigView()).getControllerConfig();
		config.addGlobalFilter(MyExceptionFilterBefore.class);
		try {
			// invoking the controller
			this.controller.process(request, this.mapper.getMapping(request));
		} finally {
			// check test results
			verify(request);
			assertEquals(1, FiltersHelper.getFilterBefore());
			assertEquals(0, FiltersHelper.getFilterAfter());
		}
	}

	@Test(expected = FilterException.class)
	public void controllerTest12() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks many atts with Filters (Global and Local)
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/many").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getRequestParameter("str")).andReturn("name1,name2,name3,name4");
		expect(request.getRequestParameter("when")).andReturn("06/11/2010");
		expect(request.getRequestParameter("int")).andReturn("5");
		replay(request);
		CotopaxiConfig config = ((CotopaxiConfigViewImpl) CotopaxiConfigView.Handler.getConfigView()).getControllerConfig();
		config.addGlobalFilter(MyExceptionFilterAfter.class);
		try {
			// invoking the controller
			this.controller.process(request, this.mapper.getMapping(request));
		} finally {
			// check test results
			verify(request);
			assertEquals(2, FiltersHelper.getFilterBefore());
			assertEquals(1, FiltersHelper.getFilterAfter());
		}
	}

	@Test
	public void controllerTest13() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test checks non given parameter
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/two/int").atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST).atLeastOnce();
		expect(request.getRequestParameter("int")).andReturn(null);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.VALIDATION_FAILED, resp.getResult());
		assertEquals("int", ((InvalidActionResponse) resp).getInvalidAttributes().iterator().next());
	}

	@Test
	public void controllerTest14() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks no logged user
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);

		try {
			// invoking the controller
			this.controller.process(request, this.mapper.getMapping(request));
			fail();
		} catch (AuthorizationException e) {
			assertEquals("/login", e.getRedirectURL());
		} finally {
			// check test results
			verify(request);
		}
	}

	@Test
	public void controllerTest15() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test checks no logged user, diferent loging url
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted2").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);

		try {
			// invoking the controller
			this.controller.process(request, this.mapper.getMapping(request));
			fail();
		} catch (AuthorizationException e) {
			assertEquals("/login2", e.getRedirectURL());
		} finally {
			// check test results
			verify(request);
		}
	}

	@Test
	public void controllerTest16() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test an logged user
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		request.setRequestParameter(UserInfo.USERNAME_ATTRIBUTE_NAME, "danilo");
		replay(request);
		ThreadProperties.setProperty(UserInfo.USER_INFO_ATTRIBUTE, new UserInfo("danilo", "developer"));

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
	}

	@Test
	public void controllerTest17() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption {
		/*
		 * This test an logged user but with wrong role
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted3").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		request.setRequestParameter(UserInfo.USERNAME_ATTRIBUTE_NAME, "danilo");
		replay(request);
		ThreadProperties.setProperty(UserInfo.USER_INFO_ATTRIBUTE, new UserInfo("danilo", "developer"));

		try {
			// invoking the controller
			this.controller.process(request, this.mapper.getMapping(request));
			fail();
		} catch (AuthorizationException e) {
			assertEquals("/forbidden", e.getRedirectURL());
		} finally {
			// check test results
			verify(request);
		}
	}

	@Test
	public void controllerTest18() throws IllegalArgumentException, AuthorizationException, FilterException, IllegalAccessException,
			PageNotFoundExeption {
		/*
		 * This test an logged user but with wrong role
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted3").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		request.setRequestParameter(UserInfo.USERNAME_ATTRIBUTE_NAME, "danilo");
		replay(request);
		ThreadProperties.setProperty(UserInfo.USER_INFO_ATTRIBUTE, new UserInfo("danilo", "admin"));

		// invoking the controller
		ActionResponse resp = this.controller.process(request, this.mapper.getMapping(request));

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
	}
}
