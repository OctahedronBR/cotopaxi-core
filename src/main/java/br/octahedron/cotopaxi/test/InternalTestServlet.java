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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.CotopaxiServlet;
import br.octahedron.cotopaxi.controller.ControllerResponse;
import br.octahedron.cotopaxi.view.response.TemplateResponse;

/**
 * @author Danilo Queiroz - dpenna.queiroz
 */
class InternalTestServlet extends CotopaxiServlet {

	private static InternalTestServlet instance = new InternalTestServlet();

	/**
	 * @return the instance
	 */
	protected synchronized static InternalTestServlet getInstance() {
		return instance;
	}

	private InternalTestServlet() {
		try {
			this.init();
		} catch (ServletException ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	private static final long serialVersionUID = 1L;
	private ControllerResponse lastResponse;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.CotopaxiServlet#processResponse(javax.servlet.http.HttpServletResponse
	 * , br.octahedron.cotopaxi.controller.ControllerResponse)
	 */
	@Override
	protected void processResponse(HttpServletResponse response, ControllerResponse controllerResponse) throws IOException, ServletException {
		this.lastResponse = controllerResponse;
		if (controllerResponse instanceof TemplateResponse) {
			this.interceptor.preRender((TemplateResponse) controllerResponse);
		}
		this.interceptor.finish();
	}

	/**
	 * Gets and clear the response for last request.
	 */
	protected ControllerResponse getLastResponse() {
		try {
			return this.lastResponse;
		} finally {
			this.lastResponse = null;
		}
	}
}
