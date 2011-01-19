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
package br.octahedron.cotopaxi.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.cotopaxi.cloudservice.URLFetchFacade;

/**
 * Declare classes to be injected on the annotated class. This should be used at the facade classes
 * and its injected classes.
 * 
 * For each class to be injected the annotated class should have a "set" method.
 * 
 * Eg.: If you want to inject an UserService class at the UserFacade class, the UserFacade class
 * should have a method "public void setUserService(UserService service)" to injection works.
 * 
 * On the above example, the UserService class, for its time, can have some injection dependencies
 * too.
 * 
 * Inject can also be used to inject some of the facade classes from <b>CloudService</b>. By now the
 * available facades to be inject are {@link DatastoreFacade}, {@link MemcacheFacade} and
 * {@link URLFetchFacade}. To inject one of this class, use the facade interface and the injector
 * will inject an implementation from the configured {@link CloudServicesFactory}.
 * 
 * Eg.:
 * 
 * <pre>
 * &#064;Inject(classes = { UserService.class })
 * public class UserFacade {
 * 	private UserService service;
 * 
 * 	public void setUserService(UserService service) {
 * 		this.service = service;
 * 	}
 * }
 * 
 * &#064;Inject(classes = { UserDAO.class })
 * public class UserService {
 * 	private UserDAO userDAO;
 * 
 * 	public void setUserDAO(UserDAO userDAO) {
 * 		this.userDAO = userDAO;
 * 	}
 * }
 * 
 * &#064;Inject(classes = { DatastoreFacade.class, MemcacheFacade.class })
 * public class UserDAO {
 * 	private DatastoreFacade datastore;
 * 	private MemcacheFacade memcache;
 * 
 * 	public void setDatastoreFacade(DatastoreFacade datastore) {
 * 		this.datastore = datastore;
 * 	}
 * 
 * 	public void setMemcacheFacade(MemcacheFacade memcache) {
 * 		this.memcache = memcache;
 * 	}
 * }
 * </pre>
 * 
 * 
 * 
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Inject {

	/**
	 * The classes to be inject
	 */
	Class<?>[] classes();

}
