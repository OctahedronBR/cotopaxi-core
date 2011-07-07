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

import static br.octahedron.cotopaxi.auth.AuthorizationRequired.CONTROLLER_NAME;
import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;

/**
 * An abstract interceptor to be used to create authorization interceptors.
 * 
 * @see ControllerInterceptor
 *  
 * @author Danilo Queiroz - daniloqueiro@octahedron.com.br
 */
public abstract class AbstractAuthorizationInterceptor extends ControllerInterceptor {

	@Override
	public final void execute(Annotation ann) {
		AuthorizationRequired auth = (AuthorizationRequired) ann;
		if ( !this.isAnswered() ) {
			String action = auth.action();
			if (CONTROLLER_NAME.equals(action)) {
				action = this.controllerName();
			}
			this.authorizeUser(action);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Class<? extends Annotation>[] getInterceptorAnnotations() {
		return (Class<? extends Annotation>[]) new Class<?>[] { AuthorizationRequired.class };
	}
	
	/**
	 * This method should check if the current logged user, is a authorized to perform the given action.
	 * 
	 * The action is the {@link AuthorizationRequired#action()}, if defined, or the controller name.
	 */
	protected abstract void authorizeUser(String controllerName);
}
