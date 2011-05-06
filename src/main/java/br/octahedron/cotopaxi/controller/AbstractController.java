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

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.inject.InstanceHandler;
import br.octahedron.cotopaxi.request.Request;
import br.octahedron.cotopaxi.response.Response;
import br.octahedron.cotopaxi.response.ResponseProcessor;
import br.octahedron.util.ReflectionUtil;

/**
 * @author Name - email@octahedron.com.br
 *
 */
public abstract class AbstractController implements Controller {
	
	protected static final Logger logger = Logger.getLogger(AbstractController.class.getName());
	private Object facade;
	private String method;
	
	public AbstractController(Class<?> facadeClass, String methodName) throws InstantiationException {
		this.facade = InstanceHandler.getInstance(facadeClass);
		this.method = methodName;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.controller.Controller#process(br.octahedron.cotopaxi.controller.Request, br.octahedron.cotopaxi.controller.ResponseProcessor)
	 */
	@Override
	public final void process(Request request, ResponseProcessor responseProcessor) {
		Response response = null;
		try {
			ControllerInput in = this.getInput(request);
			Object result = this.executeModel(in);
			response = this.successResponse(result);
		} catch (ValidationException ex) {
			response = this.invalidResponse(ex);
		} catch (Exception ex) {
			response = this.errorResponse(ex);
		} 
		responseProcessor.process(response);
		
	}
	
	private final Object executeModel(ControllerInput in) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return ReflectionUtil.invoke(facade, this.method, in.getParams());
	}

	protected abstract ControllerInput getInput(Request request) throws ValidationException;

	
	/**
	 * @param ex
	 * @return
	 */
	protected abstract Response errorResponse(Exception ex);

	/**
	 * @param ex
	 * @return
	 */
	protected abstract Response invalidResponse(ValidationException ex);

	/**
	 * @param result
	 * @return
	 */
	protected abstract Response successResponse(Object result);


	protected static class ControllerInput {

		/**
		 * @return
		 */
		public Object[] getParams() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
