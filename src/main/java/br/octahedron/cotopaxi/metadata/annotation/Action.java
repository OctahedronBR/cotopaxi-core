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
package br.octahedron.cotopaxi.metadata.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.inject.InstanceHandler;
import br.octahedron.cotopaxi.model.InputAdapter;

/**
 * Annotations to be used to indicates that a method can be invoked by the controller.
 * 
 * It provides all information needed by the controller, such as the access URL, the acceptable
 * {@link HTTPMethod}, the input parameters, provided using the {@link InputAdapter} and the
 * {@link Filter}s to be used to execute this action.
 * 
 * The url should be provided including the first SLASH and can include variables. See examples
 * below.
 * 
 * <pre>
 *  "/"
 *  "/{name}"
 *  "/{name}/{post_id}"
 * 	"/show/{id}"
 * 	"/show/{id}/{date}"
 * </pre>
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {
	/**
	 * The available HTTP methods.
	 */
	public enum HTTPMethod {
		GET, POST, GETANDPOST;
	}

	/**
	 * @return The InputAdapter for this Action
	 * @see InputAdapter
	 */
	Class<? extends InputAdapter> adapter() default InputAdapter.class;

	/**
	 * @return The Action's URL
	 */
	String url();

	/**
	 * @return The ModelMapping's HTTP Method. The default value is <code>GETANDPOST</code>
	 */
	HTTPMethod method() default HTTPMethod.GETANDPOST;

	/**
	 * The filter classes for this method.
	 */
	Class<? extends Filter>[] filters() default {};

	/**
	 * A wrapper for the Action metadata
	 */
	public static class ActionMetadata {
		private Class<? extends Filter>[] filters;
		private HTTPMethod httpMethod;
		private String url;
		private Class<? extends InputAdapter> adapterClass;
		private Method method;

		public ActionMetadata(Method method) {
			this.method = method;
			Action ann = method.getAnnotation(Action.class);
			this.adapterClass = ann.adapter();
			this.url = ann.url();
			this.httpMethod = ann.method();
			this.filters = ann.filters();
		}

		public boolean hasFilters() {
			return this.filters.length > 0;
		}

		public Collection<Class<? extends Filter>> getFilters() {
			return Arrays.asList(this.filters);
		}

		public HTTPMethod getHttpMethod() {
			return this.httpMethod;
		}

		public String getUrl() {
			return this.url;
		}

		public Class<? extends InputAdapter> getAdapterClass() {
			return this.adapterClass;
		}

		public InputAdapter getInputAdapter() {
			return InstanceHandler.getInstance(this.adapterClass);
		}

		public Method getMethod() {
			return this.method;
		}

	}
}
