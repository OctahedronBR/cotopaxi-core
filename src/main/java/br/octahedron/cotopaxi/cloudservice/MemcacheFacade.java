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
package br.octahedron.cotopaxi.cloudservice;

/**
 * Facade for Cache service.
 * 
 * @see net.sf.jsr107cache.Cache
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface MemcacheFacade {

	/**
	 * Puts an Object at cache.
	 * 
	 * @param key
	 *            The object's key
	 * @param value
	 *            The object's value
	 * @throws DisabledMemcacheException
	 */
	public abstract <T> void put(String key, T value) throws DisabledMemcacheException;

	/**
	 * Get's an Object from cache.
	 * 
	 * @param klass
	 *            the object's type
	 * @param key
	 *            The object's key
	 * @return the object to which this cache maps the specified key, or null if the cache contains
	 *         no mapping for this key.
	 */
	public abstract <T> T get(Class<T> klass, String key) throws DisabledMemcacheException;

	/**
	 * Checks if there's an object mapped for the given key
	 * 
	 * @param key
	 *            the object's key
	 * @return true if the cache has an object for the given key or false otherwise
	 */
	public abstract boolean contains(String key);

	/**
	 * Removes the object mapped by the given key from the cache
	 * 
	 * @param key
	 *            the object's key
	 */
	public abstract void remove(String key);

}
