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
package br.octahedron.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * A simple data structure to encapsulate a annotated method. It contains the annotation itself, and
 * the method which is annotated
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class AnnotatedMethod<T extends Annotation> {
	private T annotation;
	private Method method;

	public AnnotatedMethod(T ann, Method met) {
		this.annotation = ann;
		this.method = met;
	}

	/**
	 * @return the annotation
	 */
	public T getAnnotation() {
		return this.annotation;
	}

	public Method getMethod() {
		return this.method;
	}
}