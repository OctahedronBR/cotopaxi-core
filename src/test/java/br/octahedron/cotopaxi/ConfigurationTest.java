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

import org.junit.Test;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ConfigurationTest {

	@Test
	public void configureOK() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		new CotopaxiServlet().configure("br.octahedron.cotopaxi.Configurator");
	}

	@Test(expected = ClassNotFoundException.class)
	public void configureNull() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		new CotopaxiServlet().configure(null);
	}

	@Test(expected = ClassNotFoundException.class)
	public void missingConfigurator() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		new CotopaxiServlet().configure("br.octahedron.cotopaxi.Configurator2");
	}

	@Test(expected = InstantiationException.class)
	public void invalidConfigurator() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException,
			NoSuchMethodException {
		new CotopaxiServlet().configure("br.octahedron.cotopaxi.InvalidConfigurator");
	}

}
