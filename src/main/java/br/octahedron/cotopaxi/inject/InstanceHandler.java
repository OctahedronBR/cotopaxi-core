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
package br.octahedron.cotopaxi.inject;

import static br.octahedron.cotopaxi.inject.InjectionManager.containsImplementation;
import static br.octahedron.cotopaxi.inject.InjectionManager.getImplementation;
import static br.octahedron.cotopaxi.inject.InjectionManager.registerImplementation;
import static br.octahedron.cotopaxi.inject.InjectionManager.resolveDependency;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.octahedron.util.ReflectionUtil;

/**
 * You should not use this class directly. 
 * // TODO Improve this comment.
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class InstanceHandler {

	private static final Logger logger = Logger.getLogger(InstanceHandler.class.getName());

	/**
	 * Gets a <T> instance for the given {@link Class} ready to be used.
	 * 
	 * @param klass
	 *            the T's class
	 * @return The T instance.
	 * @throws InstantiationException 
	 */
	public static <T> T getInstance(Class<T> klass) throws InstantiationException {
		try {
			if (!containsImplementation(klass)) {
				T instance = createInstance(klass);
				registerImplementation(klass, instance);
			}
			return getImplementation(klass);
		} catch (Exception e) {
			throw new InstantiationException("Unable to load class " + klass);
		}
	}

	/**
	 * Creates a new instance of the given class.
	 */
	public static Object createInstance(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> klass = Class.forName(className);
		return createInstance(klass);
	}

	/**
	 * Creates a new instance of the given class.
	 */
	public static <T> T createInstance(Class<T> klass) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<? extends T> implClass = resolveDependency(klass);
		T instance = implClass.newInstance();
		inject(instance);
		return instance;
	}

	/**
	 * Checks if should inject any attribute and inject if necessary.
	 */
	protected static void inject(Object instance) {
		Collection<Field> fields = ReflectionUtil.getAnnotatedFields(instance.getClass(), Inject.class);
		// for each annotated field
		for (Field f : fields) {
			try {
				Inject inject = f.getAnnotation(Inject.class);
				// create the object to inject
				Class<?> klass = f.getType();
				Injectable injectable = klass.getAnnotation(Injectable.class);
				if (injectable != null) {
					String instancePackage = instance.getClass().getPackage().getName();
					String classPackage = klass.getPackage().getName();
					switch (injectable.scope()) {
					case PACKAGE:
						if (!classPackage.equals(instancePackage)) {
							continue;
						}
						break;
					case SUPERPACKAGE:
						if (!classPackage.substring(0, classPackage.lastIndexOf('.')).equals(instancePackage)) {
							continue;
						}
						break;
					}
				}
				Object obj;
				if (inject.newInstance()) {
					obj = getInstance(klass);
				} else {
					obj = createInstance(klass);
				}
				// gets the instance's method to inject the object created above
				logger.info("Injecting object " + obj.getClass().getSimpleName() + " into object " + instance.getClass().getSimpleName());
				Method set = ReflectionUtil.getSetMethod(klass.getSimpleName(), instance.getClass(), klass);
				set.invoke(instance, obj);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Unable to performe injection: " + ex.getLocalizedMessage(), ex);
			}
		}
	}
}
