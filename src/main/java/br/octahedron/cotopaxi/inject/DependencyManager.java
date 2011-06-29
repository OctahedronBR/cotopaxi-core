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

import java.util.HashMap;
import java.util.Map;

import br.octahedron.util.Log;

/**
 * The class responsible by manage dependencies for classes.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class DependencyManager {

	private static final Log log = new Log(DependencyManager.class);

	protected static final Map<Class<?>, Object> instances = new HashMap<Class<?>, Object>();
	protected static final Map<Class<?>, Class<?>> dependencies = new HashMap<Class<?>, Class<?>>();

	/**
	 * Registers an implementation class for a given dependency.
	 * 
	 * @param dependencyIF
	 *            The interface for a dependency
	 * @param dependencyImpl
	 *            The implementation class to be used to supply a dependency
	 */
	public static <T> void registerDependency(Class<T> dependencyIF, Class<? extends T> dependencyImpl) {
		log.debug("Registring dependency: %s as implementation for %s", dependencyImpl.getName(), dependencyIF.getName());
		dependencies.put(dependencyIF, dependencyImpl);
	}

	/**
	 * Registers an implementation class for a given dependency. The given {@link Class} can be a
	 * SuperType or a SuperInterface of the given {@link Object}.
	 * 
	 * @param dependencyIF
	 *            The interface for a dependency
	 * @param dependencyImpl
	 *            The implementation to be used to supply a dependency
	 */
	public static void registerImplementation(Class<?> dependencyIF, Object object) {
		if (dependencyIF.isAssignableFrom(object.getClass())) {
			log.debug("Registring dependency: %s as implementation for %s", object, dependencyIF.getName());
			instances.put(dependencyIF, object);
		} else {
			throw new IllegalArgumentException("The given Class should be a SuperType or SuperInterface of the given object!");
		}
	}

	public static void removeImplementation(Class<?> klass) {
		instances.remove(klass);
	}

	/**
	 * Gets the dependency's implementation for a given dependency
	 * 
	 * @param dependencyIF
	 *            the dependency interface.
	 * @return the dependency's implementation, if exists, or <code>null</code> if doesn't exists.
	 */
	@SuppressWarnings("unchecked")
	protected static <T> Class<? extends T> resolveDependency(Class<T> dependencyIF) {
		return (Class<? extends T>) ((dependencies.containsKey(dependencyIF)) ? dependencies.get(dependencyIF) : dependencyIF);
	}

	protected static boolean containsImplementation(Class<?> klass) {
		return instances.containsKey(klass);
	}

	@SuppressWarnings("unchecked")
	protected static <T> T getImplementation(Class<T> klass) {
		return (T) instances.get(klass);
	}
	
	protected void reset() {
		instances.clear();
		dependencies.clear();
	}

	/**
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> void registerDependency(String ifClass, String implClass) throws ClassNotFoundException {
		registerDependency((Class<T>)Class.forName(ifClass), (Class<? extends T>)Class.forName(implClass));
	}
}
