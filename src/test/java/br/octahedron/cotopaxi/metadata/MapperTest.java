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
package br.octahedron.cotopaxi.metadata;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.CotopaxiConfigView;
import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.controller.FakeModelFacade;
import br.octahedron.cotopaxi.controller.InvalidFacade;
import br.octahedron.cotopaxi.controller.adapters.MappingBoth;
import br.octahedron.cotopaxi.controller.adapters.MappingGet;
import br.octahedron.cotopaxi.controller.adapters.MappingGetAtts;
import br.octahedron.cotopaxi.controller.adapters.MappingGetID;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;
import br.octahedron.cotopaxi.model.InputAdapter;

/**
 * @author Danilo Penna Queiroz - daniloqueiro@octahedron.com.br
 */
public class MapperTest {

	private MetadataMapper mapper;
	private RequestWrapper request;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws SecurityException, ClassNotFoundException, NoSuchMethodException {
		this.request = createMock(RequestWrapper.class);
		CotopaxiConfigView configMock = createMock(CotopaxiConfigView.class);
		Collection facade = Arrays.asList(FakeModelFacade.class);
		expect(configMock.getModelFacades()).andReturn(facade);
		replay(configMock);
		this.mapper = new MetadataMapper(configMock);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = NoSuchMethodException.class)
	public void controllerInvalidFacades() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException {
		/*
		 * Facade didn't has an empty constructor
		 */
		CotopaxiConfigView configMock = createMock(CotopaxiConfigView.class);
		Collection facade = Arrays.asList(InvalidFacade.class);
		expect(configMock.getModelFacades()).andReturn(facade);
		replay(configMock);
		new MetadataMapper(configMock);
	}

	@Test(expected = PageNotFoundExeption.class)
	public void testNotFound1() throws PageNotFoundExeption {
		/*
		 * no handler for /{variable}/list defined - PageNotFound
		 */
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/anybody/list");
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET);
		replay(request);
		try {
			this.mapper.getMapping(request);
		} finally {
			verify(request);
		}
	}

	@Test(expected = PageNotFoundExeption.class)
	public void testNotFound2() throws PageNotFoundExeption {
		/*
		 * Wrong method - PageNotFound
		 */
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/anybody");
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.POST);
		replay(request);
		try {
			this.mapper.getMapping(request);
		} finally {
			verify(request);
		}
	}

	@Test
	public void testExtractParameters1() throws PageNotFoundExeption {
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/developer");
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET);
		expect(request.getFormat()).andReturn(null);
		expect(request.getURL()).andReturn("/developer");
		request.setRequestParameter("name", "developer");
		replay(request);
		this.mapper.getMapping(request);
		verify(request);
	}

	@Test
	public void testFindMapping() throws PageNotFoundExeption {
		// 1 - /user
		MetadataHandler user = this.mapper.getMapping("/user/", HTTPMethod.GET);
		assertNotNull(user);
		assertTrue(user.getActionMetadata().getInputAdapter() instanceof MappingBoth);
		user = this.mapper.getMapping("/user", HTTPMethod.POST);
		assertNotNull(user);
		assertTrue(user.getActionMetadata().getInputAdapter() instanceof MappingBoth);

		// 2 - /edit
		MetadataHandler edit = this.mapper.getMapping("/edit", HTTPMethod.GET);
		assertFalse(edit.getActionMetadata().getInputAdapter().getClass().equals(InputAdapter.class));
		edit = this.mapper.getMapping("/edit", HTTPMethod.POST);
		assertNotNull(edit);
		assertTrue(edit.getActionMetadata().getInputAdapter() instanceof InputAdapter);
		edit = this.mapper.getMapping("/edit/", HTTPMethod.POST);
		assertNotNull(edit);
		assertTrue(edit.getActionMetadata().getInputAdapter() instanceof InputAdapter);

		// 3 - /{name}
		MetadataHandler name = this.mapper.getMapping("/anybody", HTTPMethod.GET);
		assertNotNull(name);
		assertTrue(name.getActionMetadata().getInputAdapter() instanceof MappingGet);

		// 3 - /user/{id}
		MetadataHandler id = this.mapper.getMapping("/user/32", HTTPMethod.GET);
		assertNotNull(id);
		assertTrue(id.getActionMetadata().getInputAdapter() instanceof MappingGetID);

		// 4 - /user/{name}/{id}/view
		MetadataHandler id2 = this.mapper.getMapping("/user/anyone/32/view", HTTPMethod.GET);
		assertNotNull(id2);
		assertTrue(id2.getActionMetadata().getInputAdapter() instanceof MappingGetAtts);
		id2 = this.mapper.getMapping("/user/anyone/32/view", HTTPMethod.POST);
		assertNotNull(id2);
		assertTrue(id2.getActionMetadata().getInputAdapter() instanceof MappingGetAtts);

		// 5 - /
		MetadataHandler none = this.mapper.getMapping("/", HTTPMethod.GET);
		assertNotNull(none);
		assertTrue(id2.getActionMetadata().getInputAdapter() instanceof InputAdapter);
	}

	@Test
	public void testExtractAtts1() throws PageNotFoundExeption {
		expect(this.request.getURL()).andReturn("/user/anyone/32/view");
		expect(this.request.getHTTPMethod()).andStubReturn(HTTPMethod.GET);
		expect(this.request.getFormat()).andReturn(null);
		expect(this.request.getURL()).andReturn("/user/anyone/32/view");
		this.request.setRequestParameter("name", "anyone");
		this.request.setRequestParameter("id", "32");
		replay(this.request);
		// 4 - /user/{name}/{id}/view
		this.mapper.getMapping(this.request);
		verify(this.request);
	}

	@Test
	public void testExtractAtts2() throws PageNotFoundExeption {
		expect(this.request.getURL()).andReturn("/user/32");
		expect(this.request.getHTTPMethod()).andStubReturn(HTTPMethod.GET);
		expect(this.request.getFormat()).andReturn(null);
		expect(this.request.getURL()).andReturn("/user/32");
		this.request.setRequestParameter("id", "32");
		replay(this.request);
		// 4 - /user/{id}
		this.mapper.getMapping(this.request);
		verify(this.request);
	}
	
	@Test
	public void testExtractAtts3() throws PageNotFoundExeption, SecurityException, NoSuchMethodException {
		expect(this.request.getURL()).andReturn("/something_really_big/a/myfullname/1/452");
		expect(this.request.getHTTPMethod()).andStubReturn(HTTPMethod.GET);
		expect(this.request.getFormat()).andReturn(null);
		expect(this.request.getURL()).andReturn("/something_really_big/a/myfullname/1/452");
		this.request.setRequestParameter("name", "myfullname");
		this.request.setRequestParameter("id", "452");
		replay(this.request);
		// 4 - /user/{id}
		this.mapper.getMapping(this.request);
		verify(this.request);
	}

}
