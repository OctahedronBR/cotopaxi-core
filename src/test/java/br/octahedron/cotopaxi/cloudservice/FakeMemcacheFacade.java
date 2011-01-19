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

import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;

/**
 * @author Name - email@octahedron.com.br
 *
 */
public class FakeMemcacheFacade implements MemcacheFacade {

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.MemcacheFacade#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String key) {
		return false;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.MemcacheFacade#get(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> T get(Class<T> klass, String key) throws DisabledMemcacheException {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.MemcacheFacade#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> void put(String key, T value) throws DisabledMemcacheException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.MemcacheFacade#remove(java.lang.String)
	 */
	@Override
	public void remove(String key) {

	}

}
