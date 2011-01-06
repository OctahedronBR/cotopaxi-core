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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.CotopaxiConfigurator;
import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.metadata.annotation.Action.ActionMetadata;
import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.InvalidAttributeException;
import br.octahedron.cotopaxi.model.attribute.ModelAttribute;
import br.octahedron.cotopaxi.model.attribute.converter.ConversionException;
import br.octahedron.cotopaxi.model.response.ActionResponse;
import br.octahedron.cotopaxi.model.response.ExceptionActionResponse;
import br.octahedron.cotopaxi.model.response.InvalidActionResponse;
import br.octahedron.cotopaxi.model.response.SuccessActionResponse;
import br.octahedron.util.reflect.InstanceHandler;

/**
 * The ModelController is responsible by the model execution. It means that this entity is
 * responsible to execute the filters, extract model params from the input (request) and executes
 * the model.
 * 
 * It executes {@link Filter#doBefore(RequestWrapper)}, convert and validates the ModelAttributes,
 * executes the model, executes {@link Filter#doAfter(RequestWrapper, SuccessActionResponse)} and
 * return the generated {@link SuccessActionResponse}
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class ModelController {

	private final Logger logger = Logger.getLogger(ModelController.class.getName());
	private InstanceHandler<Object> facades = new InstanceHandler<Object>();

	/**
	 * Executes the request, it means:
	 * 
	 * <pre>
	 * 	Executes global filters (doBefore);
	 * Executes mapping filters (doBefore);
	 *  Converts model attributes;
	 *  Validates model attributes;
	 *  Executes the model;
	 *  Recovery the {@link SuccessActionResponse};
	 *  Executes the mapping filters (doAfter);
	 *  Executes the global filters (doAfter).
	 * </pre>
	 * 
	 * Then, returns the {@link ExecutableResponse}
	 * 
	 * @throws FilterException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public ActionResponse executeRequest(RequestWrapper request, ActionMetadata actionMetadata) throws FilterException, IllegalArgumentException,
			IllegalAccessException {
		ActionResponse response;
		InputAdapter adapter = actionMetadata.getInputAdapter();
		try {
			Object[] params = this.getModelParams(adapter, request);
			Object result = this.executeModel(actionMetadata.getMethod(), adapter, params);
			response = new SuccessActionResponse(result);
		} catch (ValidationException e) {
			this.logger.fine("ValidationException: " + e.getInvalidAttributes().toString());
			response = new InvalidActionResponse(e.getInvalidAttributes());
		} catch (InvocationTargetException itex) {
			Throwable cause = itex.getCause();
			this.logger.log(Level.INFO, cause.getMessage(), cause);
			response = new ExceptionActionResponse(cause);
		}
		return response;
	}

	/**
	 * Converts, validates and returns the ModelAttributes array. This array will be used to invoke,
	 * the model method. It throws an {@link ValidationException} if some attribute isn't valid.
	 */
	protected <T> Object[] getModelParams(InputAdapter adapter, RequestWrapper request) throws ValidationException {
		if (adapter.hasAttributes()) {
			// create result structures
			LinkedList<String> invalidAttributes = new LinkedList<String>();
			ArrayList<Object> params = new ArrayList<Object>();

			/*
			 * for each attribute, try to convert it, validates and add to correct result structure.
			 * if the attribute is valid (it means, can be converted and validated) adds to params
			 * otherwise, adds to invalidAttributes
			 */
			for (ModelAttribute<?> att : adapter.getAttributes()) {
				try {
					params.add(att.getAttributeValue(request));
				} catch (ConversionException e) {
					// invalid due a conversion exception
					invalidAttributes.add(att.getName());
				} catch (InvalidAttributeException ex) {
					// attribute is not valid
					invalidAttributes.add(att.getName());
				}
			}
			if (invalidAttributes.isEmpty()) {
				// all attributes are valid!
				return params.toArray();
			} else {
				throw new ValidationException(invalidAttributes);
			}
		} else {
			// there's no attributes!
			return null;
		}
	}

	/**
	 * Executes the {@link CotopaxiConfigurator} method, and return the {@link ExecutableResponse}
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws ModelException
	 */
	private Object executeModel(Method method, InputAdapter mapping, Object[] params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Object facade = this.facades.getInstance(method.getDeclaringClass());
		return method.invoke(facade, params);
	}

	/**
	 * Thrown to indicate and validation exception while getting model params.
	 */
	protected static class ValidationException extends Exception {

		private static final long serialVersionUID = -1072466743781926291L;
		private Collection<String> invalidAttributes;

		public ValidationException(Collection<String> invalidAttributes) {
			this.invalidAttributes = invalidAttributes;
		}

		public Collection<String> getInvalidAttributes() {
			return this.invalidAttributes;
		}
	}
}
