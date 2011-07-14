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

import static br.octahedron.cotopaxi.CotopaxiProperty.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.octahedron.cotopaxi.Bootloader.Booter;
import br.octahedron.cotopaxi.config.ConfigurationLoader;
import br.octahedron.cotopaxi.config.ConfigurationSyntaxException;
import br.octahedron.cotopaxi.controller.ControllerDescriptor;
import br.octahedron.cotopaxi.controller.ControllerException;
import br.octahedron.cotopaxi.controller.ControllerExecutor;
import br.octahedron.cotopaxi.controller.ControllerResponse;
import br.octahedron.cotopaxi.interceptor.InterceptorManager;
import br.octahedron.cotopaxi.route.NotFoundExeption;
import br.octahedron.cotopaxi.route.Router;
import br.octahedron.cotopaxi.view.ResponseDispatcher;
import br.octahedron.cotopaxi.view.response.TemplateResponse;
import br.octahedron.util.Log;

/**
 * The Cotopaxi Framework entry point.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class CotopaxiServlet extends HttpServlet {

	private static final long serialVersionUID = 8958499809792016589L;
	
	private static final Log log = new Log(CotopaxiServlet.class); 
	private InterceptorManager interceptor = new InterceptorManager();
	private Router router = new Router();
	private ControllerExecutor executor;
	private ResponseDispatcher dispatcher;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		try {
			log.info("Loading cotopaxi configuration...");
			Booter booter = new Bootloader.Booter();
			ConfigurationLoader loader = new ConfigurationLoader(this.router, this.interceptor, booter);
			loader.loadConfiguration();
			this.executor = new ControllerExecutor(this.interceptor);
			this.dispatcher = new ResponseDispatcher(this.interceptor);
			log.info("Cotopaxi is ready to serve...");
			booter.boot();
		} catch (FileNotFoundException ex) {
			log.error("Configuration file not found. Make sure the %s file exists", ConfigurationLoader.CONFIGURATION_FILENAME);
			throw new ServletException(ex);
		} catch (ConfigurationSyntaxException ex) {
			log.error("Error parsing configuration file: Invalid Syntax. Check you configuration file and try again");
			throw new ServletException(ex);
		} catch (Exception ex) {
			log.terror("Unexpected error loading cotopaxi!", ex);
			throw new ServletException(ex);
		}
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
	private void deliver(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		ControllerResponse controllerResponse = null;
		try {
			ControllerDescriptor controllerDesc = this.router.route(request);
			controllerResponse = this.executor.execute(controllerDesc, request);
		} catch (NotFoundExeption ex) {
			log.warning("Cannot found a controller for url %s - %s. Message: %s", request.getRequestURI(), request.getMethod(), ex.getMessage());
			Map<String, Object> output = new HashMap<String, Object>();
			output.put(getProperty(ERROR_PROPERTY), ex);
			controllerResponse = new TemplateResponse(getProperty(NOT_FOUND_TEMPLATE), 404, output, null, null, request.getLocale());
		} catch (ControllerException ex) {
			log.warning("Unexpected error executing controller for %s. Message: %s", request.getRequestURI(), ex.getMessage());
			log.twarning("ControllerException", ex);
			Map<String, Object> output = new HashMap<String, Object>();
			output.put(getProperty(ERROR_PROPERTY), ex);
			controllerResponse = new TemplateResponse(getProperty(ERROR_TEMPLATE), 500, output, null, null, request.getLocale());
		}  
		
		if (controllerResponse != null) {
			this.dispatcher.dispatch(controllerResponse, response);
	 	} else {
	 		log.error("Servlet cannot determine neither a controller, nor a error handler, for url %s", request.getRequestURI());
	 		throw new ServletException("Server cannot determine an response for request.");
	 	}
	}
}
