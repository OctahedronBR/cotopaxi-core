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

/**
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class FakeControllerInterceptor extends ControllerInterceptor {

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.interceptor.ControllerInterceptor#execute(java.lang.annotation.Annotation)
	 */
	@Override
	public void execute(Annotation ann) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.interceptor.ControllerInterceptor#getInterceptorAnnotation()
	 */
	@Override
	public Class<? extends Annotation> getInterceptorAnnotation() {
		return TestingOne.class;
	}

}
