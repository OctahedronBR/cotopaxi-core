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
package br.octahedron.cotopaxi.auth;

import static br.octahedron.cotopaxi.CotopaxiProperty.*;
import static br.octahedron.cotopaxi.auth.AuthorizationRequired.*;
import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.auth.AuthorizationRequired.NonAuthorizedConsequence;
import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;

/**
 * An abstract interceptor to be used to create authorization interceptors.
 * 
 * @see ControllerInterceptor
 * 
 * @author Danilo Queiroz - daniloqueiro@octahedron.com.br
 */
public abstract class AbstractAuthorizationInterceptor extends ControllerInterceptor {

	private static final String RESTRICTED_USER = "restricted_user";

	@Override
	public final void execute(Annotation ann) {
		AuthorizationRequired auth = (AuthorizationRequired) ann;
		if ( !this.isAnswered() ) {
			String action = auth.actionName();
			if (CONTROLLER_NAME.equals(action)) {
				action = this.controllerName();
			}
			String redirect = auth.redirect();
			if ( FORBIDDEN_PAGE.equals( redirect)) {
				redirect = getProperty(FORBIDDEN_TEMPLATE);
			}
			
			this.authorizeUser(action, auth.consequence(), redirect);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Class<? extends Annotation>[] getInterceptorAnnotations() {
		return (Class<? extends Annotation>[]) new Class<?>[] { AuthorizationRequired.class };
	}

	/**
	 * Adds the RESTRICTED_USER property to out as <code>true</code>
	 */
	protected void setRestricted() {
		out(RESTRICTED_USER, Boolean.TRUE);
	}

	/**
	 * This method should check if the current logged user, is a authorized to perform the given
	 * action.
	 * 
	 * The action is the {@link AuthorizationRequired#actionName()}, if defined, or the controller
	 * name.
	 * 
	 * @param actionName
	 *            the action being executed
	 * @param consequence
	 *            the consequence for non authorized users
	 * @param redirect 
	 */
	protected abstract void authorizeUser(String actionName, NonAuthorizedConsequence consequence, String redirect);
}
