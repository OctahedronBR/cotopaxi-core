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

/**
 * Annotation to specify that the user authentication is required to execute an {@link Action}.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginRequired {

	/**
	 * Specify the user's required roles to access the method
	 */
	String requiredRole() default "";

	/**
	 * A wrapper for LoginRequired metadata
	 * 
	 */
	public static class LoginRequiredMetadata {
		private boolean loginRequired;
		private String requiredRole;

		public LoginRequiredMetadata(Method method) {
			LoginRequired login = method.getAnnotation(LoginRequired.class);
			// extracts login information
			if (login != null) {
				this.loginRequired = true;
				this.requiredRole = login.requiredRole();
			}
		}

		public boolean isLoginRequired() {
			return this.loginRequired;
		}

		public String getRequiredRole() {
			return this.requiredRole;
		}
	}
}