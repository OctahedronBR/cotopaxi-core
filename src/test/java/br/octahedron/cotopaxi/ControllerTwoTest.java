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
import br.octahedron.cotopaxi.model.response.ActionResponse.Result;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ControllerTwoTest {

	private ModelController controller;
	private MetadataMapper mapper;

	@Before
	public void setUp() throws SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException {
		InstanceHandler instanceHandler = new InstanceHandler();
		CotopaxiConfigView configView = instanceHandler.getInstance(CotopaxiConfigView.class); 
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
		expect(request.getURL()).andReturn("/teste/contact/add").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		request.setRequestParameter("event_name", "teste");
		request.setRequestParameter("module_name", "contact");
		expect(request.getRequestParameter("event_name")).andReturn("teste");
		expect(request.getRequestParameter("module_name")).andReturn("contact");
		expect(request.getSessionAttributes()).andReturn(Collections.EMPTY_LIST);
		replay(request);

		// invoking the controller
		ActionResponse resp = this.controller.executeRequest(request, this.mapper.getMapping(request).getActionMetadata());

		// check test results
		verify(request);
		assertEquals(Result.SUCCESS, resp.getResult());
	}
}
