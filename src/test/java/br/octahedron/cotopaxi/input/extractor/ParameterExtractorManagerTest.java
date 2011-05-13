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
package br.octahedron.cotopaxi.input.extractor;

import static br.octahedron.cotopaxi.controller.ParameterScope.COOKIE;
import static br.octahedron.cotopaxi.controller.ParameterScope.HEADER;
import static br.octahedron.cotopaxi.controller.ParameterScope.REQUEST;
import static br.octahedron.cotopaxi.controller.ParameterScope.SESSION;
import static junit.framework.Assert.*;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.LinkedList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.input.InputException;
import br.octahedron.cotopaxi.input.extract.ParameterExtractorManager;
import br.octahedron.cotopaxi.request.Request;

/**
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ParameterExtractorManagerTest {

	private ParameterExtractorManager extractor = new ParameterExtractorManager();
	private Request request;

	@Before
	public void setUp() {
		request = createMock(Request.class);
	}

	@Test
	public void getParameterFromRequest() throws InputException, InstantiationException {
		// set up mock
		expect(request.getRequestParameter("str")).andReturn("lalala");
		expect(request.getRequestParameter("int")).andReturn("1000");
		// enable mock
		replay(request);
		// test
		String str = this.extractor.extractParameter(REQUEST, request, "str", String.class);
		assertEquals("lalala", str);
		Integer i = this.extractor.extractParameter(REQUEST, request, "int", Integer.class);
		assertEquals(new Integer(1000), i);
		// verify mock
		verify(request);
	}
	
	@Test
	public void getParameterFromRequest1() throws InputException, InstantiationException {
		// set up mock
		expect(request.getRequestParameter("str")).andReturn(null);
		// enable mock
		replay(request);
		// test
		assertNull(this.extractor.extractParameter(REQUEST, request, "str", String.class));
		// verify mock
		verify(request);
	}
	
	@Test
	public void getParameterFromSession() throws InputException, InstantiationException {
		// set up mock
		HttpSession session = createMock(HttpSession.class);
		expect(request.getSession()).andReturn(session).anyTimes();
		expect(session.getAttribute("int")).andReturn(new Integer(1000));
		// enable mock
		replay(request, session);
		// test
		Integer i = this.extractor.extractParameter(SESSION, request, "int", Integer.class);
		assertEquals(new Integer(1000), i);
		// verify mock
		verify(request, session);
	}
	
	@Test
	public void getParameterFromSession2() throws InputException, InstantiationException {
		// set up mock
		expect(request.getSession()).andReturn(null).anyTimes();
		// enable mock
		replay(request);
		// test
		assertNull(this.extractor.extractParameter(SESSION, request, "int", Integer.class));
		// verify mock
		verify(request);
	}
	
	@Test
	public void getParameterFromSession3() throws InputException, InstantiationException {
		// set up mock
		HttpSession session = createMock(HttpSession.class);
		expect(request.getSession()).andReturn(session).anyTimes();
		expect(session.getAttribute("int")).andReturn(null);
		// enable mock
		replay(request, session);
		// test
		assertNull(this.extractor.extractParameter(SESSION, request, "int", Integer.class));
		// verify mock
		verify(request, session);
	}
	
	@Test
	public void getParameterFromHeader() throws InputException, InstantiationException {
		// set up mock
		expect(request.getHeaderParameter("str")).andReturn("lalala");
		expect(request.getHeaderParameter("int")).andReturn("1000");
		// enable mock
		replay(request);
		// test
		String str = this.extractor.extractParameter(HEADER, request, "str", String.class);
		assertEquals("lalala", str);
		Integer i = this.extractor.extractParameter(HEADER, request, "int", Integer.class);
		assertEquals(new Integer(1000), i);
		// verify mock
		verify(request);
	}
	
	@Test
	public void getParameterFromCookie() throws InputException, InstantiationException {
		// set up mock
		LinkedList<Cookie> cookies = new LinkedList<Cookie>();
		cookies.add(new Cookie("str", "lalala"));
		cookies.add(new Cookie("a", "b"));
		cookies.add(new Cookie("int", "1000"));
		expect(request.getCookies()).andReturn(cookies).anyTimes();
		// enable mock
		replay(request);
		// test
		String str = this.extractor.extractParameter(COOKIE, request, "str", String.class);
		assertEquals("lalala", str);
		Integer i = this.extractor.extractParameter(COOKIE, request, "int", Integer.class);
		assertEquals(new Integer(1000), i);
		// verify mock
		verify(request);
	}
}
