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

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.request.RoutingProcessor;
import br.octahedron.util.FileUtil;

/**
 * 
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class CotopaxiServlet extends HttpServlet {

	private static final long serialVersionUID = 8958499809792016589L;
	public static final String CONFIGURATION_FILE_NAME = "application.properties";
	public static final String ROUTES_FILE_NAME = "routes.properties";

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.deliver(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.deliver(req, resp);
	}

	/**
	 * Dispatches a request/response.
	 */
	private void deliver(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		Request request = new RequestImpl(req, resp);
//		RequestHandler handler = RequestHandlerFactory.getRequestHandler(RequestState.PRE_ROUTE);
//		request.setStateHandler(handler); // sets the handler and starts processing it!
	}
}
