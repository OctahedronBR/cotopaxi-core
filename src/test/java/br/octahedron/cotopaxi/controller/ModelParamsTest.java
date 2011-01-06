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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.controller.ModelController.ValidationException;
import br.octahedron.cotopaxi.controller.auth.UserInfo;
import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.ObjectModelAttribute;
import br.octahedron.cotopaxi.model.attribute.RequestModelAttribute;
import br.octahedron.cotopaxi.model.attribute.SessionModelAttribute;
import br.octahedron.cotopaxi.model.attribute.converter.LongConverter;
import br.octahedron.cotopaxi.model.attribute.converter.SafeStringConverter;
import br.octahedron.cotopaxi.model.attribute.converter.StringCommaSeparatedArrayConverter;
import br.octahedron.cotopaxi.model.attribute.validator.RangeValidator;
import br.octahedron.util.DateUtil;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ModelParamsTest {

	private ModelController controller;

	@Before
	public void setUp() {
		this.controller = new ModelController();
	}

	@Test
	public void testRequestSessionParams1() throws ValidationException {
		// configure
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getSessionParameter("user")).andReturn(new UserInfo("danilo", "tester"));
		expect(request.getRequestParameter("id")).andReturn("1");
		replay(request);
		// execute
		Object[] params = this.controller.getModelParams(new AdapterOne(), request);
		// verify
		assertEquals(2, params.length);
		assertEquals(1l, params[1]);
		verify(request);
	}

	@Test(expected = ValidationException.class)
	public void testRequestSessionParams2() throws ValidationException {
		// configure
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getSessionParameter("user")).andReturn(new UserInfo("danilo", "tester"));
		expect(request.getRequestParameter("id")).andReturn(null);
		replay(request);
		// execute
		try {
			this.controller.getModelParams(new AdapterOne(), request);
		} finally {
			// verify
			verify(request);
		}
	}

	@Test
	public void testRequestSessionParams3() {
		// configure
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getSessionParameter("user")).andReturn(null);
		expect(request.getRequestParameter("id")).andReturn(null);
		replay(request);
		// execute
		try {
			this.controller.getModelParams(new AdapterOne(), request);
			fail();
		} catch (ValidationException ex) {
			assertEquals(2, ex.getInvalidAttributes().size());
		} finally {
			// verify
			verify(request);
		}
	}

	@Test
	public void testRequestSessionParams4() throws ValidationException {
		// configure
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getRequestParameter("title")).andReturn("testing");
		expect(request.getRequestParameter("labels")).andReturn("test,cotopaxi,devel");
		expect(request.getSessionParameter("date")).andReturn(DateUtil.getDate());
		expect(request.getRequestParameter("id")).andReturn("9");
		replay(request);
		// execute
		Object[] params = this.controller.getModelParams(new AdapterTwo(), request);
		// verify
		assertEquals(2, params.length);
		Something some = (Something) params[0];
		assertEquals("testing", some.getTitle());
		assertNotNull("labels", some.getLabels());
		assertNotNull("date", some.getDate());
		verify(request);
	}

	private static class AdapterOne extends InputAdapter {
		public AdapterOne() {
			this.addSessionAttribute("user", UserInfo.class);
			this.addAttribute("id", LongConverter.class, new RangeValidator<Long>(new Long(0), new Long(10)));
		}
	}

	private static class AdapterTwo extends InputAdapter {
		public AdapterTwo() {
			ObjectModelAttribute<Something> object = new ObjectModelAttribute<Something>(Something.class);
			object.addAttribute(new RequestModelAttribute<String>("title", SafeStringConverter.class));
			object.addAttribute(new RequestModelAttribute<String[]>("labels", StringCommaSeparatedArrayConverter.class));
			object.addAttribute(new SessionModelAttribute<Date>("date"));
			this.addAttribute(object);
			this.addAttribute("id", LongConverter.class, new RangeValidator<Long>(new Long(0), new Long(10)));
		}
	}
}
