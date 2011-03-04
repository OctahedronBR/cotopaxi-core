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
package br.octahedron.cotopaxi.model;

import java.util.Collection;

import br.octahedron.cotopaxi.cloudservice.DatastoreException;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.inject.Inject;

/**
 * An generic DAO interface to be extended.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class GenericDAO<T> {
	
	private Class<T> klass;
	
	@Inject
	protected DatastoreFacade datastoreFacade;
	
	public GenericDAO(Class<T> klass) {
		this.klass = klass;
	}
	
	/**
	 * @param datastoreFacade the datastoreFacade to set
	 */
	public void setDatastoreFacade(DatastoreFacade datastoreFacade) {
		this.datastoreFacade = datastoreFacade;
	}
	
	public void save(T entity) throws DatastoreException {
		this.datastoreFacade.saveObject(entity);
	}
	
	public Collection<T> getAll() {
		return this.datastoreFacade.getObjects(this.klass);
	}
	
	public T get(Object key) {
		return this.datastoreFacade.getObjectByKey(this.klass, key);
	}

	public boolean exists(Object key) {
		return this.datastoreFacade.existsObject(this.klass, key);
	}
	
	public int count() {
		return this.datastoreFacade.countObjects(this.klass);
	}
	

}
