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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a task to be executed after a given delay time. The task will be scheduled using the
 * <code>TaskManager</code>.
 * 
 * To create a Task, this class should be extended, overriding the <code>Task#run</code> method.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public abstract class Task {

	private Map<String, String> params;
	private String name;

	/**
	 * Creates a new Task, to be executed, using the given params/properties. Params are a pair of
	 * Strings, (key,value), that will be available when executing the task.
	 * 
	 * @param params
	 *            A String x String Map contain the params for task execution.
	 */
	public Task(Map<String, String> params) {
		this(null, params);
	}

	/**
	 * Creates a new Task, to be executed, using the given params/properties. Params are a pair of
	 * Strings, (key,value), that will be available when executing the task.
	 * 
	 * @param params
	 *            A String x String Map contain the params for task execution.
	 */
	public Task(String name, Map<String, String> params) {
		this.name = name;
		this.params = params;
	}

	public Task() {
		this(null, new HashMap<String, String>());
	}

	public Task(String name) {
		this(name, new HashMap<String, String>());
	}

	/**
	 * Gets the task's name.
	 * 
	 * @return the task's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get all the params names.
	 * 
	 * @return a collection that contains all params' names.
	 */
	public Collection<String> getParamsNames() {
		return this.params.keySet();
	}

	/**
	 * Gets a param's value.
	 * 
	 * @param paramName
	 *            the param's name.
	 * @return the param's value.
	 */
	public String getParam(String paramName) {
		return this.params.get(paramName);
	}

	/**
	 * This method will be called when the task be executed.
	 */
	public abstract void run();
}
