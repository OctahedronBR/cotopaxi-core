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

/**
 * Defines all Datastore Operations.
 * 
 * Its assume that all the persistent classes are mapped, and provides only a high level interface
 * for save/restore/remove data from Datastore.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface DatastoreFacade {

	/**
	 * Property name used to store the query's Cursor using <code>ThreadProperties</code>.
	 */
	public static final String CURSOR_PROPERTY = "CURSOR_PROPERTY";

	/**
	 * Sets the max size for operations that returns a collection. Number equals or lesser than zero
	 * indicates that queries should return all results founded. Its the default behavior.
	 * 
	 * @param size
	 *            the max size for queries
	 */
	public abstract void setQueriesMaxSize(int size);

	/**
	 * Gets the max queries' size.
	 * 
	 * @see DatastoreFacade#setQueriesMaxSize(int)
	 * 
	 * @return the max queries' size.
	 */
	public abstract int getMaxQueriesSize();

	/**
	 * Sets if queries should save cursors for queries. If <code>true</code> for all queries that
	 * returns a collection as result, it will saves the cursor at the <code>ThreadProperties</code>
	 * using the {@link DatastoreFacade#CURSOR_PROPERTY} as key. The default is <code>false</code>;
	 */
	public abstract void saveCursor(boolean save);

	/**
	 * @see DatastoreFacade#saveCursor(boolean)
	 * 
	 * @return <code>true</code> if the cursor is being saved, <code>false</code> otherwise.
	 */
	public abstract boolean saveCursor();

	/**
	 * Indicates if objects restored from datastore will be detached on load. Setting this to
	 * <code>true</code> will load all entities on queries, adding an overhead to queries
	 * operations. Setting to <code>false</code> objects will not be detached, being accessible only
	 * while the <code>PersistenceManager</code> referent to the query is opened.
	 * 
	 * Set it to <code>false</code> if you are using a servlet filter. The default value is
	 * <code>true</code>.
	 * 
	 * @param detach
	 *            <code>true</code> to indicates that a PersistenceManagerFilter will be used,
	 *            <code>false</code> otherwise.
	 */
	public abstract void detachObjectsOnQuery(boolean detach);

	/**
	 * @return <code>true</code> if objects are being detached on queries, <code>false</code>
	 *         otherwise.
	 */
	public abstract boolean detachObjectsOnQuery();

	/**
	 * Saves the given object at the Datastore.
	 * 
	 * @param persistentObject
	 *            the object to be save.
	 * @throws DatastoreException
	 *             If some error occurs writing at the datastore.
	 */
	public abstract <T> void saveObject(T persistentObject) throws DatastoreException;

	/**
	 * Saves the given objects at the Datastore
	 * 
	 * @param persistentObjects
	 * @throws DatastoreException
	 *             If some error occurs writing at the datastore.
	 */
	public abstract <T> void saveAllObjects(Collection<T> persistentObjects) throws DatastoreException;

	/**
	 * Deletes the given object from Datastore.
	 * 
	 * @param persistentObject
	 *            the object to be deleted.
	 * @throws DatastoreException
	 *             If some error occurs writing at the Datastore.
	 */
	public abstract <T> void deleteObject(T persistentObject) throws DatastoreException;

	/**
	 * Deletes all given objects from Datastore.
	 * 
	 * @param persistentObjects
	 *            The objects to be deleted.
	 * @throws DatastoreException
	 *             If some error occurs writing at the datastore.
	 */
	public abstract <T> void deleteAllObjects(Collection<T> persistentObjects) throws DatastoreException;

	/**
	 * Deletes <b>all</b> objects of the given class.
	 * 
	 * @param klass
	 *            The object class.
	 * @throws DatastoreException
	 *             If some error occurs writing at the datastore.
	 */
	public abstract <T> void deleteObjects(Class<T> klass) throws DatastoreException;

	/**
	 * Deletes <b>all</b> objects of the given class that matches the given filter.
	 * 
	 * @param klass
	 *            The object's class.
	 * @param filter
	 *            the filter used to select entities do be deleted
	 * @throws DatastoreException
	 *             If some error occurs writing at the datastore.
	 */
	public abstract <T> void deleteObjectsByQuery(Class<T> klass, String filter) throws DatastoreException;

	/**
	 * Gets an object with the given key.
	 * 
	 * @param klass
	 *            The object's class.
	 * @param key
	 *            The object's key
	 * @return The object for the given key or null if theresn't an object for the given key.
	 */
	public abstract <T> T getObjectByKey(Class<T> klass, Object key);

	/**
	 * Checks if exists an object, of the given type, and with the given key.
	 * 
	 * @param klass
	 *            The object's class.
	 * @param key
	 *            The object's key
	 * @return <code>true</code> if object exists, <code>false</code> otherwise
	 */
	public abstract <T> boolean existsObject(Class<T> klass, Object key);

	/**
	 * Gets all objects of the given class.
	 * 
	 * @see DatastoreFacade#saveCursor(boolean)
	 * @see DatastoreFacade#setQueriesMaxSize(int)
	 * 
	 * @param klass
	 *            The objects' class
	 * @return a collection with all objects of the given class.
	 */
	public abstract <T> Collection<T> getObjects(Class<T> klass);

	/**
	 * Gets all objects of the given class that matches the given filter.
	 * 
	 * @see DatastoreFacade#saveCursor(boolean)
	 * @see DatastoreFacade#setQueriesMaxSize(int)
	 * 
	 * @param klass
	 *            The objects' class
	 * @param filter
	 *            The objects' filter
	 * @return a collection with all objects of the given class.
	 */
	public abstract <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter);

	/**
	 * Gets all objects of the given class that matches the given filter, ordered by the given
	 * ordering atributes.
	 * 
	 * @see DatastoreFacade#saveCursor(boolean)
	 * @see DatastoreFacade#setQueriesMaxSize(int)
	 * 
	 * @param klass
	 *            The objects' class
	 * @param filter
	 *            The objects' filter
	 * @param orderingAtts
	 *            The objects' ordering atribuites.
	 * @return a collection with all objects of the given class.
	 */

	public abstract <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter, String orderingAtts);

	/**
	 * Counts all objects of the given class.
	 * 
	 * @see DatastoreFacade#saveCursor(boolean)
	 * @see DatastoreFacade#setQueriesMaxSize(int)
	 * 
	 * @param klass
	 *            The objects' class
	 * @return the number of objects of the given class.
	 */
	public abstract <T> int countObjects(Class<T> klass);

	/**
	 * Counts all objects of the given class that matches the given filter.
	 * 
	 * @see DatastoreFacade#saveCursor(boolean)
	 * @see DatastoreFacade#setQueriesMaxSize(int)
	 * 
	 * @param klass
	 *            The objects' class
	 * @param filter
	 *            The objects' filter
	 * @return the number of objects of the given class.
	 */
	public abstract <T> int countObjectsByQuery(Class<T> klass, String filter);

}