package br.octahedron.util.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * An instance handler for instances of a type. It's responsible by assure that only an instance of
 * a given type is used. An instance is retrieved given the object's <code>Class</code>.
 * 
 * When a new instance is created, the method {@link InstanceHandler#createdInstance(Object)} is
 * invoked, thus is possible, if necessary, overwrite this method to perform some operation over the
 * brand new object.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 * @param <T>
 *            The handled type.
 */
public class InstanceHandler<T> {

	private Map<Class<? extends T>, T> filters = new HashMap<Class<? extends T>, T>();

	/**
	 * Gets a <T> instance for the given Filter's class ready to be used.
	 * 
	 * @param klass
	 *            the T's class
	 * @return The T instance.
	 */
	@SuppressWarnings("unchecked")
	public final T getInstance(Class<? extends T> klass) {
		try {
			if (!this.filters.containsKey(klass)) {
				T instance = (T) ReflectionUtil.createInstance(klass);
				this.filters.put(klass, instance);
				this.createdInstance(instance);
			}
			return this.filters.get(klass);
		} catch (Exception e) {
			throw new InstanceLoadException("Unable to loading klass " + klass, e);
		}
	}

	/**
	 * This method is called when a new T instance is created. It can be used to perform some
	 * operation over the new instance. The default implementation do nothing.
	 * 
	 * @param instance
	 *            The brand new instance.
	 */
	protected void createdInstance(T instance) {
		// It should be overwritten
	}

}