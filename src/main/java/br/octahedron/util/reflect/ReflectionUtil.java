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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

/**
 * This class provides reflection utilities methods.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class ReflectionUtil {

	public static Object createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		return clazz.newInstance();
	}

	public static Object createInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return createInstance(Class.forName(className));
	}

	public static <T extends Annotation> Collection<AnnotatedMethod<T>> getAnnotatedMethods(Class<?> klass, Class<T> annClass) {
		final Collection<AnnotatedMethod<T>> result = new LinkedList<AnnotatedMethod<T>>();
		AnnotatedMethodListener<T> listener = new AnnotatedMethodListener<T>() {
			@Override
			public void annotatedMethodFound(AnnotatedMethod<T> annMet) {
				result.add(annMet);
			}
		};
		getAnnotatedMethods(klass, annClass, listener);
		return result;
	}

	public static <T extends Annotation> void getAnnotatedMethods(Class<?> klass, Class<T> annClass, AnnotatedMethodListener<T> listener) {
		Method[] methods = klass.getMethods();
		for (Method met : methods) {
			if (met.isAnnotationPresent(annClass)) {
				T ann = met.getAnnotation(annClass);
				listener.annotatedMethodFound(new AnnotatedMethod<T>(ann, met));
			}
		}
	}

	public static <T extends Annotation> Collection<Field> getAnnotatedFields(Class<?> klass, Class<T> annClass) {
		Field[] fields = klass.getDeclaredFields();
		Collection<Field> result = new LinkedList<Field>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(annClass)) {
				result.add(field);
			}
		}
		return result;
	}

	public static Object getFieldValue(Field field, Object instance) {
		// getField method name
		String lower = field.getName();
		StringBuilder buf = new StringBuilder();
		buf.append("get");
		buf.append(Character.toUpperCase(lower.charAt(0)));
		buf.append(lower.substring(1));
		// find the method
		try {
			Method met = instance.getClass().getMethod(buf.toString(), new Class[0]);
			return invoke(instance, met, new Object[0]);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T extends Annotation> Object getAnnotatedFieldValue(Object instance, Class<T> annClass) {
		Object result = null;
		Collection<Field> fields = getAnnotatedFields(instance.getClass(), annClass);
		if (fields.size() > 0) {
			Field f = fields.iterator().next();
			result = getFieldValue(f, instance);
		}
		return result;
	}

	public static Object invoke(Object instance, Method method, Object... args) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return method.invoke(instance, args);
	}

	public static Class<?> getClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

}
