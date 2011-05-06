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
package br.octahedron.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class store properties only for the current <code>Thread</code>.
 * 
 * This entity is a singleton.
 * 
 * @see Properties
 * @see ThreadLocal
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class ThreadProperties {
	
	public enum ThreadPropertiesKeys {
		REQUEST, RESPONSE, REQUESTED_URL;
	}

	private static ThreadLocal<Map<ThreadPropertiesKeys, Object>> threadProperties = new ThreadLocalProperties();

	/**
	 * Gets a property's value for the current Thread.
	 * 
	 * @return the property key, or <code>null</code> if the property doesn't exists.
	 */
	public static Object getProperty(ThreadPropertiesKeys key) {
		return threadProperties.get().get(key);
	}

	/**
	 * Gets a property's value for the current Thread.
	 * 
	 * @return the property key, or <code>null</code> if the property doesn't exists.
	 */
	public static boolean containsProperty(String key) {
		return threadProperties.get().containsKey(key);
	}

	public static Set<ThreadPropertiesKeys> keys() {
		return threadProperties.get().keySet();
	}

	/**
	 * Saves a property's value for the current Thread.
	 * 
	 * @param key
	 *            The property's name/key.
	 * @param value
	 *            the property's value.
	 */
	public static void setProperty(ThreadPropertiesKeys key, Object value) {
		threadProperties.get().put(key, value);
	}

	public static boolean isEmpty() {
		return threadProperties.get().isEmpty();
	}

	/**
	 * Cleans the current Thread's properties.
	 */
	public static void clear() {
		threadProperties.remove();
	}

	/**
	 * A Thread Local that store a <code>Properties</code> instance.
	 */
	private static class ThreadLocalProperties extends ThreadLocal<Map<ThreadPropertiesKeys, Object>> {
		@Override
		protected Map<ThreadPropertiesKeys, Object> initialValue() {
			return new HashMap<ThreadPropertiesKeys, Object>();
		}
	}
}
