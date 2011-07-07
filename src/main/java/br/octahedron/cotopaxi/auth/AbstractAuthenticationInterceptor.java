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

import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.auth.AuthenticationRequired.AuthenticationLevel;
import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;

/**
 * An abstract interceptor to be used to create authentication interceptors.
 * 
 * @see ControllerInterceptor
 * 
 * @author Danilo Queiroz - daniloqueiro@octahedron.com.br
 */
public abstract class AbstractAuthenticationInterceptor extends ControllerInterceptor {

	public static final String CURRENT_USER_EMAIL = "%%%current_user_email%%%";

	@Override
	public final void execute(Annotation ann) {
		AuthenticationLevel level = AuthenticationLevel.AUTHENTICATE_AND_VALID;
		if (ann instanceof AuthenticationRequired) {
			AuthenticationRequired auth = (AuthenticationRequired) ann;
			level = auth.authenticationLevel();
		}

		this.checkUserAuthentication();
		if (level == AuthenticationLevel.AUTHENTICATE_AND_VALID && !this.isAnswered()) {
			this.checkUserValidation();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Class<? extends Annotation>[] getInterceptorAnnotations() {
		return (Class<? extends Annotation>[]) new Class<?>[] { AuthenticationRequired.class, AuthorizationRequired.class };
	}

	/**
	 * @param username
	 *            Adds the user as logged at current session
	 */
	protected void setUserLogged(String username) {
		session(CURRENT_USER_EMAIL, username);
	}

	/**
	 * Check if the user is logged at current session. Should use the setUserLogger to set the user
	 * as logged in current session.
	 * 
	 * @param username The user's name
	 * @return <code>true</code> if the user is logged in current session, <code>false</code> otherwise
	 */
	protected boolean isUserLogged() {
		return session(CURRENT_USER_EMAIL) != null;
	}
	
	/**
	 * Gets the current logged user's name. It should be used with the setUserLogged(String) method.
	 * 
	 * @param username The user's name
	 * @return <code>true</code> if the user is logged in current session, <code>false</code> otherwise
	 */
	protected String currentLoggedUser() {
		return (String) session(CURRENT_USER_EMAIL);
	}

	/**
	 * This method should check if the current user requesting to access a data is authenticate.
	 * 
	 * You should handle all authentication check operation, and redirect the user, if necessary
	 * (e.g.: if user not logged). For this you can use the method <code>redirect</code>.
	 * 
	 * If the user is authenticate, nothing should be done.
	 */
	protected abstract void checkUserAuthentication();

	/**
	 * This method should check if the current logged user (it assumes that the user is logged), is
	 * a valid user.
	 * 
	 * The valid user criteria is application specific and can be used, for example, to block users.
	 * 
	 * This method is called only if the user is logged, and the {@link AuthenticationLevel} is
	 * AUTHENTICATE_AND_VALID;
	 */
	protected abstract void checkUserValidation();
}
