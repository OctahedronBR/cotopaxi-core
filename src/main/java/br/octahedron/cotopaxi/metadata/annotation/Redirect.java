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
import java.util.logging.Level;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.InputHandler;
import br.octahedron.cotopaxi.controller.RedirectStrategy;
import br.octahedron.cotopaxi.model.response.ActionResponse;

/**
 * Annotations to be used to indicates that an action can result on a redirection. This annotations
 * provides an {@link RedirectStrategy} that will be used to determine if the response will be
 * redirect and the redirect URL, if necessary.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Redirect {

	Class<? extends RedirectStrategy> strategy();

	/**
	 * A wrapper for the Redirect metadata
	 */
	public static class RedirectMetadata {
		
		// Instance Handler for Mappings
		private RedirectStrategy redirectStrategy;

		public RedirectMetadata(Method method) {
			try {
				Redirect ann = method.getAnnotation(Redirect.class);
				if (ann != null) {
					this.redirectStrategy = ann.strategy().newInstance();
				}
			} catch (Exception ex) {
				Logger logger = Logger.getLogger(RedirectMetadata.class.getName());
				logger.log(Level.WARNING, "Unable to create RedirectStrategy: " + ex.getMessage(), ex);
			}

			if (this.redirectStrategy == null) {
				this.redirectStrategy = new RedirectStrategy() {
					@Override
					public boolean shouldRedirect(ActionResponse response) {
						return false;
					}

					@Override
					public String getRedirectURL(ActionResponse response, InputHandler inputHandler) {
						return null;
					}
				};
			}
		}

		public RedirectStrategy getRedirectStrategy() {
			return redirectStrategy;
		}
	}
}
