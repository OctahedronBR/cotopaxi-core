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

import java.util.HashMap;
import java.util.Map;

import br.octahedron.cotopaxi.inject.InstanceHandler;

/**
 * Creates a task to be executed after a given delay time. The task will be scheduled using the
 * <code>TaskManager</code>.
 * 
 * To create a Task, this class should be extended, overriding the <code>Task#run</code> method.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class Task {
	
	private static TaskEnqueuer enqueuer;
	static {
		InstanceHandler handler = new InstanceHandler();
		enqueuer = handler.getInstance(TaskEnqueuer.class);
	}

	private Map<String, String> params;
	private String url;
	private String queue;
	private long delay;

	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url The url to be executed 
	 */
	public Task(String url) {
		this(url, new HashMap<String, String>());
	}
	
	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url The url to be executed
	 * @param delay the number of milliseconds delay before execution of the task. 	 
	 */
	public Task(String url, long delay) {
		this(url, new HashMap<String, String>(), delay);
	}

	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url The url to be executed
	 * @param params
	 *            A String x String Map contain the params for task execution.
	 */
	public Task(String url, Map<String, String> params) {
		this(url, params, null);
	}
	
	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url The url to be executed
	 * @param params
	 *            A String x String Map contain the params for task execution.
	 * @param delay the number of milliseconds delay before execution of the task.
	 */
	public Task(String url, Map<String, String> params, long delay) {
		this(url, params, null, delay);
	}
	
	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url url The url to be executed
	 * @param queue The queue to enqueue the task
	 * @param delay the number of milliseconds delay before execution of the task. 
	 */
	public Task(String url, String queue, long delay) {
		this(url, new HashMap<String, String>(), queue, delay);
	}
	
	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url url The url to be executed
	 * @param queue The queue to enqueue the task
	 */
	public Task(String url, String queue) {
		this(url, new HashMap<String, String>(), queue);
	}
	
	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url The url to be executed
	 * @param params
	 *            A String x String Map contain the params for task execution.
	 * @param queue The queue to enqueue the task
	 */
	public Task(String url, Map<String, String> params, String queue) {
		this(url, params, queue, 0);
	}
	
	/**
	 * Creates a new Task to be executed.
	 * 
	 * @param url The url to be executed
	 * @param params
	 *            A String x String Map contain the params for task execution.
	 * @param queue The queue to enqueue the task
	 * @param queue The queue to enqueue the task
	 */
	public Task(String url, Map<String, String> params, String queue, long delay) {
		this.url = url;
		this.params = params;
		this.queue = queue;
		this.delay = delay;
	}

	/**
	 * Enqueue this task to be executed.
	 * The task will run only after be enqueued.
	 */
	public void enqueue() {
		enqueuer.enqueue(this);
	}

	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * @return the delay
	 */
	public long getDelay() {
		return delay;
	}
}
