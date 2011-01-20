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
package br.octahedron.cotopaxi.view;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.CotopaxiConfigView;
import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.ResponseWrapper;
import br.octahedron.cotopaxi.metadata.MetadataHandler;
import br.octahedron.cotopaxi.metadata.MetadataMapper;
import br.octahedron.cotopaxi.metadata.PageNotFoundExeption;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;
import br.octahedron.cotopaxi.model.response.ExceptionActionResponse;
import br.octahedron.cotopaxi.model.response.InvalidActionResponse;
import br.octahedron.cotopaxi.model.response.SuccessActionResponse;
import br.octahedron.cotopaxi.view.formatter.Formatter;
import br.octahedron.cotopaxi.view.formatter.FormatterNotFoundException;
import br.octahedron.cotopaxi.view.formatter.VelocityFormatter;
import br.octahedron.cotopaxi.view.response.ResultCode;
import br.octahedron.cotopaxi.view.response.ViewResponse;
import br.octahedron.cotopaxi.view.response.ViewResponseBuilder;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ViewResponseBuilderTest {

	private MetadataMapper mapper;
	private ViewResponseBuilder builder;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws SecurityException, ClassNotFoundException, NoSuchMethodException {
		System.setProperty("cotopaxi.test", "true");
		CotopaxiConfigView configMock = createMock(CotopaxiConfigView.class);
		Collection facade = Arrays.asList(FakeFacade.class);
		expect(configMock.getModelFacades()).andReturn(facade);
		replay(configMock);
		this.mapper = new MetadataMapper(configMock);
		this.builder = new ViewResponseBuilder(CotopaxiConfigView.getInstance());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void messageTemplateCodeTest1() throws PageNotFoundExeption, FormatterNotFoundException, IOException {
		// configuration
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/test1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);
		MetadataHandler metadata = this.mapper.getMapping(request);
		// execution
		ViewResponse resp = this.builder.getViewResponse(Locale.US, null, new SuccessActionResponse(request, null), metadata);
		FakeResponseWrapper rw = new FakeResponseWrapper();
		resp.dispatch(rw);
		VelocityFormatter fmt = (VelocityFormatter) rw.getFormatter();
		Map<String, Object> atts = fmt.getAttributes();
		// assertion
		assertEquals(ResultCode.SUCCESS, rw.getResultCode());
		assertEquals("FakeFacade_lalala.vm", fmt.getTemplate());
		assertTrue(atts.containsKey(TemplatesAttributes.MESSAGE_ON_SUCCESS.getAttributeKey()));
		assertEquals("OK", atts.get(TemplatesAttributes.MESSAGE_ON_SUCCESS.getAttributeKey()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void messageTemplateCodeTest2() throws PageNotFoundExeption, FormatterNotFoundException, IOException {
		// configuration
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/test1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);
		MetadataHandler metadata = this.mapper.getMapping(request);
		// execution
		ViewResponse resp = this.builder.getViewResponse(Locale.US, null, new ExceptionActionResponse(request, new NullPointerException()), metadata);
		FakeResponseWrapper rw = new FakeResponseWrapper();
		resp.dispatch(rw);
		VelocityFormatter fmt = (VelocityFormatter) rw.getFormatter();
		Map<String, Object> atts = fmt.getAttributes();
		// assertion
		assertEquals(ResultCode.INTERNAL_ERROR, rw.getResultCode());
		assertEquals("FakeFacade_lalala_error.vm", fmt.getTemplate());
		assertTrue(atts.containsKey(TemplatesAttributes.MESSAGE_ON_ERROR.getAttributeKey()));
		assertEquals("FAILED", atts.get(TemplatesAttributes.MESSAGE_ON_ERROR.getAttributeKey()));
		assertTrue(atts.containsKey(TemplatesAttributes.EXCEPTION_CLASS_ATTRIBUTE.getAttributeKey()));
		assertEquals(NullPointerException.class.getName(), atts.get(TemplatesAttributes.EXCEPTION_CLASS_ATTRIBUTE.getAttributeKey()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void messageTemplateCodeTest3() throws PageNotFoundExeption, FormatterNotFoundException, IOException {
		// configuration
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/test1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		expect(request.getSessionAttributes()).andReturn(Arrays.asList("test"));
		expect(request.getSessionAttribute("test")).andReturn(new Object());
		replay(request);
		MetadataHandler metadata = this.mapper.getMapping(request);
		// execution
		ViewResponse resp = this.builder.getViewResponse(Locale.US, null, new InvalidActionResponse(request, "name", "age"), metadata);
		FakeResponseWrapper rw = new FakeResponseWrapper();
		resp.dispatch(rw);
		VelocityFormatter fmt = (VelocityFormatter) rw.getFormatter();
		Map<String, Object> atts = fmt.getAttributes();
		// assertion
		assertEquals(ResultCode.BAD_REQUEST, rw.getResultCode());
		assertEquals("FakeFacade_lalala_invalid.vm", fmt.getTemplate());
		assertTrue(atts.containsKey(TemplatesAttributes.MESSAGE_ON_VALIDATION_FAILS.getAttributeKey()));
		assertEquals("INVALID", atts.get(TemplatesAttributes.MESSAGE_ON_VALIDATION_FAILS.getAttributeKey()));
		assertTrue(atts.containsKey(TemplatesAttributes.INVALIDATION_FIELDS_ATTRIBUTE.getAttributeKey()));
		Iterator<String> fields = ((Collection<String>) atts.get(TemplatesAttributes.INVALIDATION_FIELDS_ATTRIBUTE.getAttributeKey())).iterator();
		assertEquals("name", fields.next());
		assertEquals("age", fields.next());
	}

	public class FakeResponseWrapper extends ResponseWrapper {

		private Formatter formatter;
		private ResultCode resultCode;

		public FakeResponseWrapper() {
			super(null);
		}

		@Override
		public void setResultCode(ResultCode code) {
			this.resultCode = code;
		}

		@Override
		public void render(Formatter formatter) throws IOException {
			this.formatter = formatter;
		}

		public Formatter getFormatter() {
			return this.formatter;
		}

		public ResultCode getResultCode() {
			return this.resultCode;
		}
	}
}
