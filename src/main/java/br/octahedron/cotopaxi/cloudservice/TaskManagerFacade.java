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

import java.util.Map;
import java.util.Timer;

import br.octahedron.cotopaxi.cloudservice.common.Task;

/**
 * Class responsible by execute tasks. Tasks can be added to a specific queue. Queues are like
 * pools, that can't have different rate and/or policies for task scheduling. For more details, you
 * should look at the TaskManager implementation being used.
 * 
 * 
 * @See {@link Timer}
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface TaskManagerFacade {

	/**
	 * Adds a Task to be executed after the given delay. The task will be add to the default queue
	 * and will be added only if the queue doesn't contains a task with the same name.
	 * 
	 * After added to the queue, task can't be changed, and modifying the params Map will has no
	 * effects.
	 * 
	 * @param task
	 *            The task to be added.
	 * @param delayMillis
	 *            The delay, in milliseconds, to execute the task.
	 */
	public abstract void add(Task task, long delayMillis);

	/**
	 * Adds a Task to be executed after the given delay. It'll try to add the task to the queue with
	 * the given name, or will add to the default queue if the given queue doesn't exists.
	 * 
	 * After added to the queue, task can't be changed, and modifying the params Map will has no
	 * effects.
	 * 
	 * @param task
	 *            The task to be added.
	 * @param delayMillis
	 *            The delay, in milliseconds, to execute the task.
	 * @param queueName
	 *            The queue's name.
	 */
	public abstract void add(Task task, long delayMillis, String queueName);

	/**
	 * Creates a new queue with the given name (Optional Operation).
	 * 
	 * @param queueName
	 *            The queue's name
	 * @throws UnsupportedOperationException
	 *             If the operation is not supported.
	 */
	public abstract void createQueue(String queueName) throws UnsupportedOperationException;

	/**
	 * Creates a new queue with the given name (Optional Operation). It uses the given properties to
	 * configure the queue.
	 * 
	 * @param queueName
	 *            The queue's name
	 * @param queueProperties
	 *            the queue's properties
	 * @throws UnsupportedOperationException
	 *             If the operation is not supported.
	 */
	public abstract void createQueue(String queueName, Map<String, String> queueProperties) throws UnsupportedOperationException;

}
