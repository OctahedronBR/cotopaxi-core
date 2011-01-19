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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.CotopaxiConfigView;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.cotopaxi.cloudservice.URLFetchFacade;
import br.octahedron.util.reflect.InstanceHandler;
import br.octahedron.util.reflect.ReflectionUtil;

/**
 * An instance Handler that performs IOC injection before return the current instance.
 * 
 * @see Inject
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class InjectorInstanceHandler extends InstanceHandler<Object> {
	
	private static final Logger logger = Logger.getLogger(InjectorInstanceHandler.class.getName());
	
	@Override
	protected void createdInstance(Object instance) {
		try {
			this.checkIOC(instance);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Unable to performe injection: " + ex.getLocalizedMessage(), ex);
		} 
	}
	
	
	/** 
	 * Checks if should inject any attribute and inject if necessary.
	 */
	private void checkIOC(Object instance) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Inject injectAnn = ReflectionUtil.getAnnotation(instance.getClass(), Inject.class);
		if ( injectAnn != null ) {
			// must Inject!
			for(Class<?> c : injectAnn.classes()) {
				Method set = ReflectionUtil.getSetMethod(c.getSimpleName(), instance.getClass(), c);
				Object obj = null;
				// TODO add suport to more facades!
				if ( c.equals(DatastoreFacade.class) ) {
					obj = CotopaxiConfigView.getInstance().getCloudServicesFactory().createDatastoreFacade();
				} else if (c.equals(MemcacheFacade.class)) {
					obj = CotopaxiConfigView.getInstance().getCloudServicesFactory().createMemcacheFacade(); 
				} else if (c.equals(URLFetchFacade.class)) {
					obj = CotopaxiConfigView.getInstance().getCloudServicesFactory().createURLFetchFacade(); 
				} else {
					obj = this.getInstance(c);
				}
				set.invoke(instance, obj);
			}
		}
	}

}
