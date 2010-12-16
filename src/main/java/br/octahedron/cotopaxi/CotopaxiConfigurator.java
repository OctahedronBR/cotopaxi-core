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
package br.octahedron.cotopaxi;

/**
 * Indicates that a class is a ModelFacades and can be used by controller.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface CotopaxiConfigurator {

	/**
	 * Configures the Facade, based on Controller's configuration. This method is invoked by the
	 * controller when server starts.
	 * 
	 * Take a look at the {@link CotopaxiConfig} class to see which options you can configure at you
	 * <code>ModelFacade</code>. In general you should configure the
	 * <code>CloudServiceFactory</code>.
	 * 
	 * @param configure
	 *            the controller's configuration.
	 */
	public void configure(CotopaxiConfig configure);

}
