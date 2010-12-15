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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.DistributedLock;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;

/**
 * @author nome - email@octahedron.com.br
 * 
 */
public class MemcacheQueueTest {

	private DistributedQueue<String> queue;
	private MemcacheFacade cacheFacade;
	private DistributedLock monitor;
	private String cacheKey = "test";

	@Before
	public void setUp() {
		this.cacheFacade = createMock(MemcacheFacade.class);
		this.monitor = createMock(DistributedLock.class);
		this.queue = new DistributedQueue<String>(this.cacheKey, this.cacheFacade, this.monitor);
	}

	@Test
	public void testAddQueue() throws DisabledMemcacheException, TimeoutException {
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(false);
		this.cacheFacade.put(this.cacheKey, Arrays.asList("cloud"));
		this.monitor.unlock();
		replay(this.monitor, this.cacheFacade);

		assertEquals(true, this.queue.add("cloud"));
		verify(this.monitor, this.cacheFacade);
	}

	@Test
	public void testGetQueue() throws DisabledMemcacheException, TimeoutException {
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(false);
		this.cacheFacade.put(this.cacheKey, Arrays.asList("cloud"));
		this.monitor.unlock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(true);
		expect(this.cacheFacade.get(ArrayList.class, this.cacheKey)).andReturn(this.createArrayList());
		replay(this.monitor, this.cacheFacade);

		assertEquals(true, this.queue.add("cloud"));
		assertEquals("cloud", this.queue.get());
		verify(this.monitor, this.cacheFacade);
	}

	@Test
	public void testGetAllQueue() throws DisabledMemcacheException, TimeoutException {
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(false);
		this.cacheFacade.put(this.cacheKey, Arrays.asList("cloud"));
		this.monitor.unlock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(true);
		expect(this.cacheFacade.get(ArrayList.class, this.cacheKey)).andReturn(this.createArrayList());
		replay(this.monitor, this.cacheFacade);

		assertEquals(true, this.queue.add("cloud"));
		assertEquals(this.createArrayList(), this.queue.getAll());
		verify(this.monitor, this.cacheFacade);
	}

	@Test
	public void testRemoveQueue() throws DisabledMemcacheException, TimeoutException {
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(false);
		this.cacheFacade.put(this.cacheKey, Arrays.asList("cloud"));
		this.monitor.unlock();
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(true);
		expect(this.cacheFacade.get(ArrayList.class, this.cacheKey)).andReturn(this.createArrayList());
		this.cacheFacade.put(this.cacheKey, Collections.emptyList());
		this.monitor.unlock();
		replay(this.monitor, this.cacheFacade);

		assertEquals(true, this.queue.add("cloud"));
		assertEquals("cloud", this.queue.remove());
		verify(this.monitor, this.cacheFacade);
	}

	@Test
	public void testRemoveElementsQueue() throws DisabledMemcacheException, TimeoutException {
		ArrayList<String> list = this.createArrayList();

		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(false);
		this.cacheFacade.put(this.cacheKey, Arrays.asList("cloud"));
		this.monitor.unlock();
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(true);
		expect(this.cacheFacade.get(ArrayList.class, this.cacheKey)).andReturn(list);
		this.cacheFacade.put(this.cacheKey, Arrays.asList("cloud", "computing"));
		this.monitor.unlock();
		this.monitor.lock();
		expect(this.cacheFacade.contains(this.cacheKey)).andReturn(true);
		expect(this.cacheFacade.get(ArrayList.class, this.cacheKey)).andReturn(list);
		this.cacheFacade.put(this.cacheKey, Collections.emptyList());
		this.monitor.unlock();
		replay(this.monitor, this.cacheFacade);

		assertEquals(true, this.queue.add("cloud"));
		assertEquals(true, this.queue.add("computing"));
		assertEquals(true, this.queue.removeElements(2));
		verify(this.monitor, this.cacheFacade);
	}

	private ArrayList<String> createArrayList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("cloud");

		return list;
	}

}
