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

import static br.octahedron.cotopaxi.CotopaxiConfigView.HTML_FORMAT;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * TODO COMMENTS!!!
 * 
 * @author Danilo Penna Queiroz - email@octahedron.com.br
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Response {

	public static final String DEFAULT_RETURN_NAME = "[default]";
	public static final String DEFAULT_FORMAT = HTML_FORMAT;

	/**
	 * The result's value name to be used at the template and, if required, store on session. If not
	 * given the value will be add to the attributes map and session the simple class name, in lower
	 * case.
	 * 
	 * @see Class#getSimpleName()
	 */
	String returnName() default DEFAULT_RETURN_NAME;

	/**
	 * Used to indicate that the return value should be stored on Session.
	 */
	boolean storeOnSession() default false;

	/**
	 * Specify the acceptable formats for response. The first format will be considered as the
	 * default format to be used.
	 * 
	 * <b>This array can't be empty.</b>
	 * 
	 * @return A list of all possible formats for response.
	 */
	String[] acceptableFormats() default { DEFAULT_FORMAT };

	/**
	 * A wrapper for Response metadata
	 */
	public static class ResponseMetadata {
		private String defaultFormat = DEFAULT_FORMAT;
		private String[] formats = { DEFAULT_FORMAT };
		private boolean storeOnSession = false;
		private String returnName = DEFAULT_RETURN_NAME;

		public ResponseMetadata(Method method) {
			Response response = method.getAnnotation(Response.class);
			// extracts response information
			if (response != null) {
				this.returnName = response.returnName();
				this.storeOnSession = response.storeOnSession();
				this.formats = response.acceptableFormats();
				this.defaultFormat = response.acceptableFormats()[0];
			}
			if (this.returnName.equals(DEFAULT_RETURN_NAME)) {
				this.returnName = method.getReturnType().getSimpleName().toLowerCase();
			}
		}

		public String getDefaultFormat() {
			return this.defaultFormat;
		}

		public String[] getFormats() {
			return this.formats;
		}

		public boolean isStoreOnSession() {
			return this.storeOnSession;
		}

		public String getReturnName() {
			return this.returnName;
		}
	}
}
