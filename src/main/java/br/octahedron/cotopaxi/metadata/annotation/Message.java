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

import br.octahedron.cotopaxi.model.response.ActionResponse;

/**
 * This annotation specifies messages to be shown on view for each {@link ActionResponse}.
 * 
 * It provides an alternative way to return messages to view, apart from the return method.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Message {

	public static final String DEFAULT_MESSAGE = "[default]";

	/**
	 * @return The template to be used on model successful execution
	 */
	String onSuccess() default DEFAULT_MESSAGE;

	/**
	 * @return The template to be used on model error execution.
	 */
	String onError() default DEFAULT_MESSAGE;

	/**
	 * @return The template to be used on model input validation fails.
	 */
	String onValidationFails() default DEFAULT_MESSAGE;

	/**
	 * A wrapper for Message metatada
	 */
	public static class MessageMetadata {
		private String onSuccess;
		private String onError;
		private String onValidationFail;

		public MessageMetadata(Method method) {
			// try load from annotation and override defaults
			Message message = method.getAnnotation(Message.class);
			if (message != null) {
				// on success
				if (!message.onSuccess().equals(DEFAULT_MESSAGE)) {
					this.onSuccess = message.onSuccess();
				}
				// on error
				if (!message.onError().equals(DEFAULT_MESSAGE)) {
					this.onError = message.onError();
				}
				// on validationFails
				if (!message.onValidationFails().equals(DEFAULT_MESSAGE)) {
					this.onValidationFail = message.onValidationFails();
				}
			}
		}

		/**
		 * @return the message to be shown on success or <code>null</code> if no message set.
		 */
		public String getOnSuccess() {
			return this.onSuccess;
		}

		/**
		 * @return the message to be shown on error or <code>null</code> if no message set.
		 */
		public String getOnError() {
			return this.onError;
		}

		/**
		 * @return the message to be shown on validation fail or <code>null</code> if no message
		 *         set.
		 */
		public String getOnValidationFail() {
			return this.onValidationFail;
		}
	}

}
