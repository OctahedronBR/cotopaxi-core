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
import br.octahedron.cotopaxi.input.InputException;
import br.octahedron.cotopaxi.input.ModelInput;
import br.octahedron.cotopaxi.input.extract.ParameterExtractorManager;
import br.octahedron.cotopaxi.request.Request;
import br.octahedron.cotopaxi.response.Response;
import br.octahedron.cotopaxi.response.ResponseProcessor;
import br.octahedron.util.ReflectionUtil;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class AbstractController implements Controller {
	
	protected static final Logger logger = Logger.getLogger(AbstractController.class.getName());
	@SuppressWarnings("unused")
	private ParameterExtractorManager extractor = new ParameterExtractorManager();
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
			ModelInput input = this.getInput(request);
			Object result = this.executeModel(this.validate(input));
			response = this.successResponse(result);
		} catch (InputException ex) {
			response = this.invalidResponse(ex);
		} catch (Exception ex) {
			response = this.errorResponse(ex);
		} 
		responseProcessor.process(response);
		
	}
	
	/**
	 * @param input
	 * @return
	 */
	private ModelInput validate(ModelInput input) {
		return input;
	}

	private final Object executeModel(ModelInput in) throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return ReflectionUtil.invoke(facade, this.method, in.getModelParameters());
	}

	protected abstract ModelInput getInput(Request request) throws InputException;
	/*	Method template
	 * 
	 * ModelInput in = new ModelInput();
	 * in.addParameter(parameterName, this.extractor.extractParameter(scope,request,parameterName,parameterClass));
	 * ...
	 * return in;
	 */

	
	/**
	 * @param ex
	 * @return
	 */
	protected abstract Response errorResponse(Exception ex);

	/**
	 * @param ex
	 * @return
	 */
	protected abstract Response invalidResponse(InputException ex);

	/**
	 * @param result
	 * @return
	 */
	protected abstract Response successResponse(Object result);
}
