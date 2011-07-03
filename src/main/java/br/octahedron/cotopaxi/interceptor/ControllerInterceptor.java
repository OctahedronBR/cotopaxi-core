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
package br.octahedron.cotopaxi.interceptor;

import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.controller.Controller;

/**
 * An interceptor that is executed just before {@link Controller} methods. Interceptors as fired
 * using annotations on {@link Controller} methods, so that is necessary to annotated the methods
 * which the interceptor should act, and the interceptor should declare which annotations classes it
 * is interested.
 * 
 * A {@link ControllerInterceptor} can get parameter from input, add object to output and even
 * changes the execution flow by calling the redirect or render, or one of it shortcuts, to deliver
 * an response without the Controller be executed.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class ControllerInterceptor extends Controller {

	/**
	 * Execute this Interceptor. This method is called just before the controller execution.
	 * 
	 * @param ann
	 *            The annotation relative to this interceptor
	 */
	public abstract void execute(Annotation ann);

	/**
	 * Get all the annotations this intercept is interested.
	 */
	public abstract Class<? extends Annotation>[] getInterceptorAnnotations();

}
