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
package br.octahedron.util;

/**
 * @author Danilo Penna Queiroz - dpennaqueiroz@octahedron.com.br
 */
public class TestUtil {
	
	private static final String TESTING_PROPERTY = "TESTING";
	
	public static void enableTestMode() {
		System.setProperty(TESTING_PROPERTY, Boolean.TRUE.toString());
	}
	
	public static void disableTestMode() {
		System.setProperty(TESTING_PROPERTY, Boolean.FALSE.toString());
	}
	
	public static boolean isTestModeEnabled() {
		return Boolean.parseBoolean(System.getProperty(TESTING_PROPERTY, Boolean.FALSE.toString()));
	}

}
