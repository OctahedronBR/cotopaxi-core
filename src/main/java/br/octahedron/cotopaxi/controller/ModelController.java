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

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.config.CotopaxiConfigView;
import br.octahedron.cotopaxi.config.CotopaxiConfigurator;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.metadata.annotation.Action.ActionMetadata;
import br.octahedron.cotopaxi.model.ActionResponse;
import br.octahedron.cotopaxi.model.ExceptionActionResponse;
import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.InvalidActionResponse;
import br.octahedron.cotopaxi.model.SuccessActionResponse;
import br.octahedron.cotopaxi.model.attribute.ModelAttribute;
import br.octahedron.cotopaxi.model.attribute.converter.ConversionException;
import br.octahedron.cotopaxi.model.attribute.converter.TypeConverter;
import br.octahedron.cotopaxi.model.attribute.validator.Validator;
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
class ModelController {

	private final Logger logger = Logger.getLogger(ModelController.class.getName());
	private CotopaxiConfigView config;
	private InstanceHandler<Filter> filters = new InstanceHandler<Filter>();
	private InstanceHandler<TypeConverter<?>> converters = new InstanceHandler<TypeConverter<?>>();
	private InstanceHandler<Object> facades = new InstanceHandler<Object>();

	protected ModelController(CotopaxiConfigView config) {
		this.config = config;
	}

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
	protected ActionResponse executeRequest(RequestWrapper request, ActionMetadata actionMetadata) throws FilterException, IllegalArgumentException,
			IllegalAccessException {
		// Process Filters
		if (this.config.hasGlobalFilters()) {
			this.executeFiltersBefore(this.config.getGlobalFilters(), request);
		}
		if (actionMetadata.hasFilters()) {
			this.executeFiltersBefore(actionMetadata.getFilters(), request);
		}
		ActionResponse response = this.getModelResponse(request, actionMetadata);
		// Process Filters
		if (this.config.hasGlobalFilters()) {
			this.executeFiltersAfter(this.config.getGlobalFilters(), request, response);
		}
		if (actionMetadata.hasFilters()) {
			this.executeFiltersAfter(actionMetadata.getFilters(), request, response);
		}

		return response;
	}

	/**
	 * Gets the model response. It converts the parameters, execute model and generate the
	 * ModelResponse
	 * 
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private ActionResponse getModelResponse(RequestWrapper request, ActionMetadata actionMetadata) throws IllegalArgumentException,
			IllegalAccessException {
		// Executes model
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
	@SuppressWarnings("unchecked")
	private <T> Object[] getModelParams(InputAdapter mapping, RequestWrapper request) throws ValidationException {
		if (mapping.hasAttributes()) {
			// create result structures
			LinkedList<String> invalidAttributes = new LinkedList<String>();
			ArrayList<Object> params = new ArrayList<Object>();

			/*
			 * for each attribute, try to convert it, validates and add to correct result structure.
			 * if the attribute is valid (it means, can be converted and validated) adds to params
			 * otherwise, adds to invalidAttributes
			 */
			for (ModelAttribute<?> att : mapping.getAttributes()) {
				try {
					String strAttValue = request.getRequestParameter(att.getName());
					if (strAttValue == null) {
						// if parameter was not passed, use default value
						strAttValue = att.getDefaultValue();
					}

					// try to convert
					TypeConverter<T> converter = (TypeConverter<T>) this.converters.getInstance(att.getTypeConverterClass());
					T converted = converter.convert(strAttValue);
					// check if there's a validator register for the att
					if (att.hasValidator()) {
						// try validate
						Validator<T> validator = (Validator<T>) att.getValidator();
						if (validator.isValid(converted)) {
							// that's okay, add it to params!
							params.add(converted);
						} else {
							// invalid due a validator
							invalidAttributes.add(att.getName());
						}
					} else {
						// if there isn't a validator, just add the converted att to params list
						params.add(converted);
					}
				} catch (ConversionException e) {
					// invalid due a conversion exception
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
	 * Execute the {@link Filter#doBefore(RequestWrapper)}
	 */
	private void executeFiltersBefore(Collection<Class<? extends Filter>> filters, RequestWrapper request) throws FilterException {
		for (Class<? extends Filter> filterClass : filters) {
			Filter filter = this.filters.getInstance(filterClass);
			filter.doBefore(request);
		}
	}

	/**
	 * Execute the {@link Filter#doAfter(RequestWrapper, Response)}
	 */
	private void executeFiltersAfter(Collection<Class<? extends Filter>> filters, RequestWrapper request, ActionResponse response)
			throws FilterException {
		for (Class<? extends Filter> filterClass : filters) {
			Filter filter = this.filters.getInstance(filterClass);
			filter.doAfter(request, response);
		}
	}

	/**
	 * Thrown to indicate and validation exception while getting model params.
	 */
	private static class ValidationException extends Exception {

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
