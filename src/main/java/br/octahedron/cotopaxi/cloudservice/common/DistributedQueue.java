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
package br.octahedron.cotopaxi.cloudservice.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.DistributedLock;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;

/**
 * A data structure based on FIFO (First In, First Out) criterion using a Memcache service, to store
 * elements, and some extra functions.
 * 
 * All operations that modify this queue (add, remove and removeElements) are thread-safe.
 * 
 * @see Queue
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class DistributedQueue<T> {

	private final Logger logger = Logger.getLogger(DistributedQueue.class.getName());
	private MemcacheFacade cache;
	private DistributedLock monitor;
	private String cacheKey;

	public DistributedQueue(String queueName, CloudServicesFactory factory) {
		this(queueName, factory.createMemcacheFacade(), factory.createDistributedLock(queueName));
	}

	public DistributedQueue(String queueName, MemcacheFacade cache, DistributedLock monitor) {
		this.cacheKey = queueName;
		this.cache = cache;
		this.monitor = monitor;
	}

	/**
	 * Inserts the specified element at the final of this queue.
	 * 
	 * @param element
	 * @return <code>true</code> if this collection changed as a result of the call, or
	 *         <code>false</code> otherwise.
	 */
	public boolean add(T element) {
		try {
			this.monitor.lock();
			List<T> list = this.getAll();
			boolean isAdded = list.add(element);
			this.cache.put(this.cacheKey, list);

			return isAdded;
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to put elements in Memcache. Memcache Disabled!");
		} catch (TimeoutException e) {
			this.logger.warning("Unable to put elements in Memcache. Timeout!");
		} finally {
			this.monitor.unlock();
		}
		return false;
	}

	/**
	 * Retrieves, but does not remove, the first element of this queue, or returns <code>null</code>
	 * if this queue is empty.
	 * 
	 * @return The first element of this queue, or <code>null</code> if this queue is empty.
	 */
	public T get() {
		try {
			ArrayList<T> list = this.getAll();
			if (!list.isEmpty()) {
				T element = list.get(0);
				return element;
			}
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to put elements in Memcache. Memcache Disabled!");
		}
		return null;
	}

	/**
	 * Removes the first element o this queue and returns the element.
	 * 
	 * @return the removed element or <code>null</code> if this queue is empty.
	 */
	public T remove() {
		try {
			this.monitor.lock();
			List<T> list = this.getAll();
			if (!list.isEmpty()) {
				T element = list.remove(0);
				this.cache.put(this.cacheKey, list);

				return element;
			}
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to put elements in Memcache. Memcache Disabled!");
		} catch (TimeoutException e) {
			this.logger.finest("Unable to put elements in Memcache. Timeout!");
		} finally {
			this.monitor.unlock();
		}
		return null;
	}

	/**
	 * Removes from this queue all of the elements whose index is between 0, inclusive, and
	 * <code>toIndex</code>, exclusive.
	 * 
	 * @param toIndex
	 *            Index after last element to be removed
	 * @return <code>true</code> if this collection changed as a result of the call, or
	 *         <code>false</code> otherwise.
	 */
	@SuppressWarnings("unchecked")
	public boolean removeElements(int toIndex) {
		try {
			this.monitor.lock();
			ArrayList<T> list = this.getAll();
			if (list != null) {
				List<T> sublist = list.subList(0, toIndex);
				// clone to avoid ConcurrentModificationException
				ArrayList<T> listCloned = ((ArrayList<T>) list.clone());
				boolean isRemoved = listCloned.removeAll(sublist);
				this.cache.put(this.cacheKey, listCloned);

				return isRemoved;
			}
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to put elements in Memcache. Memcache Disabled!");
		} catch (TimeoutException e) {
			this.logger.finest("Unable to put elements in Memcache. Timeout!");
		} finally {
			this.monitor.unlock();
		}
		return false;
	}

	/**
	 * Retrieves, but does not remove, all elements of this queue, or returns null if this queue is
	 * empty.
	 * 
	 * @return All elements of this queue, or an empty list if this queue is empty.
	 * @throws DisabledMemcacheException
	 */
	@SuppressWarnings("unchecked")
	protected ArrayList<T> getAll() throws DisabledMemcacheException {
		ArrayList<T> list;
		if (this.cache.contains(this.cacheKey)) {
			list = this.cache.get(ArrayList.class, this.cacheKey);
			return list;
		} else {
			return new ArrayList<T>();
		}
	}
}
