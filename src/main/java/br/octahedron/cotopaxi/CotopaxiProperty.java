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

import java.text.DateFormat;
import java.text.NumberFormat;

import br.octahedron.cotopaxi.i18n.LocaleManager;

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
	 * The TemplateRender to be used to render templates. Default:
	 * br.octahedron.cotopaxi.view.render.VelocityTemplateRender
	 * 
	 * @see br.octahedron.cotopaxi.view.render.TemplateRender
	 * 
	 */
	TEMPLATE_RENDER("br.octahedron.cotopaxi.view.render.VelocityTemplateRender"),
	/**
	 * Templates' folder. Default: templates/
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
	 * The property used to binds validation errors on controller top level. Default: invalid
	 */
	INVALID_PROPERTY("invalid"),
	/**
	 * The application base url. Default: http://localhost:8080
	 */
	APPLICATION_BASE_URL("http://localhost:8080"),
	/**
	 * The application I18N files base folder. Default: i18n/
	 */
	I18N_FOLDER("i18n"),
	/**
	 * TODO improve doc I18N Base file. Default: master
	 * 
	 * @see LocaleManager
	 */
	I18N_BASE_FILE("master"),
	/**
	 * List the supported Locales for internationalization (i18n). Locales should be separated by
	 * commas. E.g.: en_US, en, pt_BR
	 * 
	 * Default: en
	 */
	I18N_SUPPORTED_LOCALES("en"),
	/**
	 * The property used to binds internationalization String map. Default: i18n
	 */
	I18N_PROPERTY("i18n"),
	/**
	 * The property used to binds internationalization {@link DateFormat}. Default: dateFormat
	 */
	I18N_DATE_FORMAT_PROPERTY("dateFormat"),
	/**
	 * The property used to binds internationalization {@link NumberFormat}. Default: numberFormat
	 */
	I18N_NUMBER_FORMAT_PROPERTY("numberFormat");

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

	public static String getProperty(String property) {
		return System.getProperty(property);
	}
}
