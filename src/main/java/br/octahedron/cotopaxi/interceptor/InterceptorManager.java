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

import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.util.Log;
import br.octahedron.util.ReflectionUtil;

/**
 * This entity is responsible by load and execute interceptors.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class InterceptorManager {

	private static final Log log = new Log(InterceptorManager.class);
	private Map<Class<? extends Annotation>, ControllerInterceptor> controllerInterceptors = new HashMap<Class<? extends Annotation>, ControllerInterceptor>();
	private Collection<ResponseDispatcherInterceptor> responseInterceptors = new LinkedList<ResponseDispatcherInterceptor>(); 

	/**
	 * TODO comment
	 */
	public void addInterceptor(String interceptorClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
			Class<?> klass = ReflectionUtil.getClass(interceptorClass);
			if (ControllerInterceptor.class.isAssignableFrom(klass)) {
				ControllerInterceptor interceptor = (ControllerInterceptor) klass.newInstance();
				for(Class<? extends Annotation> ann : interceptor.getInterceptorAnnotations()) {
					this.controllerInterceptors.put(ann, interceptor);
				}
			} else if (ResponseDispatcherInterceptor.class.isAssignableFrom(klass)) {
				ResponseDispatcherInterceptor interceptor = (ResponseDispatcherInterceptor) klass.newInstance();
				this.responseInterceptors.add(interceptor);
			} else {
				log.error("The %s isn't an interceptor class", interceptorClass);
			}
	}

	/**
	 * Execute the {@link ControllerInterceptor} for the given annotations.
	 * 
	 * @param annotations
	 *            the {@link Controller} method {@link Annotation}
	 */
	public void execute(Annotation[] annotations) {
		for (Annotation ann : annotations) {
			if (controllerInterceptors.containsKey(ann)) {
				ControllerInterceptor interceptor = this.controllerInterceptors.get(ann);
				log.debug("Executing ControllerInterceptor %s with annotation %s", interceptor.getClass(), annotations.getClass());
				interceptor.execute(ann);
			}
		}
	}

	/**
	 * Executes the {@link ResponseDispatcherInterceptor} get writer. 
	 */
	public Writer getWriter(Writer writer) {
		for(ResponseDispatcherInterceptor interceptor : responseInterceptors) {
			log.debug("Executing ResponseDispatcherInterceptor get writer %s", interceptor.getClass());
			writer = interceptor.getWriter(writer);
		}
		return writer;
	}

	/**
	 * Executes the {@link ResponseDispatcherInterceptor} finish. 
	 */
	public void finish() {
		for(ResponseDispatcherInterceptor interceptor : responseInterceptors) {
			log.debug("Executing ResponseDispatcherInterceptor finish %s", interceptor.getClass());
			interceptor.finish();
		}
	}
}