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

import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.cloudservice.DistributedCounter;
import br.octahedron.cotopaxi.cloudservice.DistributedLock;
import br.octahedron.cotopaxi.cloudservice.EmailFacade;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.cotopaxi.cloudservice.TaskManagerFacade;
import br.octahedron.cotopaxi.cloudservice.URLFetchFacade;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class FakeFactory implements CloudServicesFactory {

	@Override
	public DatastoreFacade createDatastoreFacade() {
		return new FakeDatastoreFacade();
	}

	@Override
	public DistributedCounter createDistributedCounter(String counterName) {
		return null;
	}

	@Override
	public DistributedLock createDistributedLock(String lockName) {
		return null;
	}

	@Override
	public EmailFacade createEmailFacade() {
		return null;
	}

	@Override
	public MemcacheFacade createMemcacheFacade() {
		return new FakeMemcacheFacade();
	}

	@Override
	public TaskManagerFacade createTaskManagerFacade() {
		return null;
	}

	@Override
	public URLFetchFacade createURLFetchFacade() {
		return null;
	}

}
