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

	public static <T extends Annotation> T getAnnotation(Class<?> klass, Class<T> annClass) {
		if (klass.isAnnotationPresent(annClass)) {
			return klass.getAnnotation(annClass);

		} else {
			return null;
		}
	}

	/**
	 * Gets all the {@link Method}s annotated with the given {@link Annotation}.
	 */
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

	/**
	 * Gets all the {@link Method}s annotated with the given {@link Annotation}. The result is
	 * returned using the given {@link AnnotatedMethodListener}.
	 * 
	 * @see ReflectionUtil#getAnnotatedMethods(Class, Class)
	 */
	public static <T extends Annotation> void getAnnotatedMethods(Class<?> klass, Class<T> annClass, AnnotatedMethodListener<T> listener) {
		Method[] methods = klass.getMethods();
		for (Method met : methods) {
			if (met.isAnnotationPresent(annClass)) {
				T ann = met.getAnnotation(annClass);
				listener.annotatedMethodFound(new AnnotatedMethod<T>(ann, met));
			}
		}
	}

	/**
	 * Get all {@link Field}s annotated with the given {@link Annotation}
	 */
	public static <T extends Annotation> Collection<Field> getAnnotatedFields(Class<?> klass, Class<T> annClass) {
		Collection<Field> result = new LinkedList<Field>();
		Field[] fields = klass.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(annClass)) {
				result.add(field);
			}
		}
		return result;
	}

	/**
	 * Gets the "set" method for a field with the given name. Eg.: if the given field name is
	 * "name", it will look for a method called setName.
	 */
	public static Method getSetMethod(String fieldName, Class<?> klass, Class<?> paramType) throws SecurityException, NoSuchMethodException {
		String name = (fieldName.length() > 2) ? "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) : fieldName.toUpperCase();
		return klass.getMethod(name, paramType);
	}

	/**
	 * Gets a field's value using the "get" method. Eg.: for a field named "name" it will use the
	 * method getName to recover the field's value.
	 */
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

	/**
	 * Get an annotated field's value.
	 * 
	 * @see ReflectionUtil#getFieldValue(Field, Object)
	 */
	public static <T extends Annotation> Object getAnnotatedFieldValue(Object instance, Class<T> annClass) {
		Object result = null;
		Collection<Field> fields = getAnnotatedFields(instance.getClass(), annClass);
		if (fields.size() > 0) {
			Field f = fields.iterator().next();
			result = getFieldValue(f, instance);
		}
		return result;
	}

	/**
	 * Invokes the given object {@link Method}.
	 */
	public static Object invoke(Object instance, Method method, Object... params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return method.invoke(instance, params);
	}

	/**
	 * Gets an {@link Class} by name.
	 */
	public static Class<?> getClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

}
