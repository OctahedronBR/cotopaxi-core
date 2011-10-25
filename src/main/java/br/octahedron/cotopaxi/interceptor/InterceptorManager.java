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

import static br.octahedron.cotopaxi.inject.Injector.getInstance;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.view.response.TemplateResponse;
import br.octahedron.util.Log;
import br.octahedron.util.ReflectionUtil;

/**
 * This entity is responsible by load and execute interceptors.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class InterceptorManager {

	private static final Log log = new Log(InterceptorManager.class);
	private Map<Class<? extends Annotation>, ControllerInterceptor> controllerInterceptors = new LinkedHashMap<Class<? extends Annotation>, ControllerInterceptor>();
	//;fields are protected for tests
	protected Collection<TemplateInterceptor> templateInterceptors = new LinkedList<TemplateInterceptor>();
	protected Collection<FinalizerInterceptor> finalizerInterceptors = new LinkedList<FinalizerInterceptor>();

	/**
	 * Adds a new interceptor to the application
	 */
	public void addInterceptor(String interceptorClass) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> klass = ReflectionUtil.getClass(interceptorClass);
		if (ControllerInterceptor.class.isAssignableFrom(klass)) {
			ControllerInterceptor interceptor = (ControllerInterceptor) getInstance(klass);
			this.addControllerInterceptor(interceptor);
		} else if (TemplateInterceptor.class.isAssignableFrom(klass)) {
			TemplateInterceptor interceptor = (TemplateInterceptor) getInstance(klass);
			this.templateInterceptors.add(interceptor);
		} else if (FinalizerInterceptor.class.isAssignableFrom(klass)) {
			FinalizerInterceptor interceptor = (FinalizerInterceptor) getInstance(klass);
			this.finalizerInterceptors.add(interceptor);
		} else {
			log.error("The %s isn't an interceptor class", interceptorClass);
		}
	}

	/**
	 * Used by tests
	 */
	protected void addControllerInterceptor(ControllerInterceptor interceptor) {
		Class<? extends Annotation> ann = interceptor.getInterceptorAnnotation();
		this.controllerInterceptors.put(ann, interceptor);
	}

	/**
	 * Execute the {@link ControllerInterceptor} for the given annotations.
	 * 
	 * @param annotations
	 *            the {@link Controller} method {@link Annotation}
	 */
	public void execute(AnnotatedElement controllerAnnotatedElement) {
		for (Class<? extends Annotation> annClass : this.controllerInterceptors.keySet()) {
			Annotation ann = controllerAnnotatedElement.getAnnotation(annClass);
			if (ann != null) {
				ControllerInterceptor interceptor = this.controllerInterceptors.get(annClass);
				log.debug("Executing ControllerInterceptor %s with annotation %s", interceptor.getClass(), annClass);
				interceptor.execute(ann);
			}
		}
	}

	/**
	 * Executes the {@link TemplateInterceptor} get writer.
	 */
	public void preRender(TemplateResponse templateResponse) {
		for (TemplateInterceptor interceptor : this.templateInterceptors) {
			log.debug("Executing TemplateResponse preRender(%s): %s", templateResponse.getClass(), interceptor.getClass());
			interceptor.preRender(templateResponse);
		}
	}

	/**
	 * Executes the {@link TemplateInterceptor} finish.
	 */
	public void finish() {
		for (FinalizerInterceptor interceptor : this.finalizerInterceptors) {
			log.debug("Executing FinalizerInterceptor finish %s", interceptor.getClass());
			interceptor.finish();
		}
	}

}
