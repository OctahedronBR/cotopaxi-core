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
 * The CotopaxiFramework's properties and default values.
 * 
 * To overwrite this values, just use {@link System#setProperty(String, String)} or pass the
 * property value using the -D parameter.
 * 
 * E.g.: System.setProperty("TEMPLATE_FOLDER","tpls/");
 * 
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public enum CotopaxiProperty {

	/**
	 * Velocity templates' folder. Default: templates/
	 */
	TEMPLATE_FOLDER("templates/"),
	/**
	 * Server Error (500) template. Default: error.vm 
	 */
	ERROR_TEMPLATE("error.vm"),
	/**
	 * Forbidden Error (403) template. Default: forbidden.vm 
	 */
	FORBIDDEN_TEMPLATE("notauthorized.vm"),
	/**
	 * Bad Request Error (400) template. Default: invalid.vm 
	 */
	INVALID_TEMPLATE("invalid.vm"),
	/**
	 * Not Found Error (404) template. Default: notFound.vm 
	 */
	NOT_FOUND_TEMPLATE("notfound.vm"),
	/**
	 * The property used to binds errors on controller top level. Default: error
	 */
	ERROR_PROPERTY("error"),
	/**
	 * The application base url. Default: http://localhost:8080
	 */
	APPLICATION_BASE_URL("http://localhost:8080");

	private String defaultValue;

	private CotopaxiProperty(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String defaultValue() {
		return this.defaultValue;
	}

	public static String getProperty(CotopaxiProperty property) {
		return System.getProperty(property.name(), property.defaultValue());
	}
}
