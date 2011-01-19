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

import java.util.Collection;

import br.octahedron.cotopaxi.cloudservice.DatastoreException;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 *
 */
public class FakeDatastoreFacade implements DatastoreFacade {

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#countObjects(java.lang.Class)
	 */
	@Override
	public <T> int countObjects(Class<T> klass) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#countObjectsByQuery(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> int countObjectsByQuery(Class<T> klass, String filter) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#deleteAllObjects(java.util.Collection)
	 */
	@Override
	public <T> void deleteAllObjects(Collection<T> persistentObjects) throws DatastoreException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#deleteObject(java.lang.Object)
	 */
	@Override
	public <T> void deleteObject(T persistentObject) throws DatastoreException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#deleteObjects(java.lang.Class)
	 */
	@Override
	public <T> void deleteObjects(Class<T> klass) throws DatastoreException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#deleteObjectsByQuery(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> void deleteObjectsByQuery(Class<T> klass, String filter) throws DatastoreException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#detachObjectsOnLoad(boolean)
	 */
	@Override
	public void detachObjectsOnLoad(boolean detach) {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#detachObjectsOnLoad()
	 */
	@Override
	public boolean detachObjectsOnLoad() {
		return false;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#existsObject(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> boolean existsObject(Class<T> klass, Object key) {
		return false;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#getMaxQueriesSize()
	 */
	@Override
	public int getMaxQueriesSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#getObjectByKey(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T getObjectByKey(Class<T> klass, Object key) {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#getObjects(java.lang.Class)
	 */
	@Override
	public <T> Collection<T> getObjects(Class<T> klass) {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#getObjectsByQuery(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter) {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#getObjectsByQuery(java.lang.Class, java.lang.String, java.lang.String)
	 */
	@Override
	public <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter, String orderingAtts) {
		return null;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#saveAllObjects(java.util.Collection)
	 */
	@Override
	public <T> void saveAllObjects(Collection<T> persistentObjects) throws DatastoreException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#saveCursor(boolean)
	 */
	@Override
	public void saveCursor(boolean save) {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#saveCursor()
	 */
	@Override
	public boolean saveCursor() {
		return false;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#saveObject(java.lang.Object)
	 */
	@Override
	public <T> void saveObject(T persistentObject) throws DatastoreException {

	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.cloudservice.DatastoreFacade#setQueriesMaxSize(int)
	 */
	@Override
	public void setQueriesMaxSize(int size) {

	}

}
