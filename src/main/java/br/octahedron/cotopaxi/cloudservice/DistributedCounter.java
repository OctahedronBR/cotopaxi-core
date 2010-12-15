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
 * A simple distributed counter that simulates multiple counters storing each counter with a key.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public interface DistributedCounter {

	/**
	 * Counts one unit value for a specific key.
	 * 
	 * @param key
	 *            The key of individual counter.
	 * @return Actual value of counter.
	 */
	public long count();

	/**
	 * Gets actual value of counter.
	 * 
	 * @param key
	 *            The key of individual counter.
	 * @return Actual value of counter.
	 */
	public long getValue();

	/**
	 * Gets this counter's name
	 * 
	 * @return the counter's name
	 */
	public String getName();

}
