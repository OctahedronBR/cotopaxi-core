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
 * A factory interface for CloudServices implementations.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface CloudServicesFactory {

	public DatastoreFacade createDatastoreFacade();

	public MemcacheFacade createMemcacheFacade();

	public EmailFacade createEmailFacade();

	public TaskManagerFacade createTaskManagerFacade();

	public URLFetchFacade createURLFetchFacade();

	public DistributedCounter createDistributedCounter(String counterName);

	public DistributedLock createDistributedLock(String lockName);

}