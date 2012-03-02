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
package br.octahedron.cotopaxi.i18n;

import static br.octahedron.cotopaxi.CotopaxiProperty.I18N_DATE_FORMAT_PROPERTY;
import static br.octahedron.cotopaxi.CotopaxiProperty.I18N_NUMBER_FORMAT_PROPERTY;
import static br.octahedron.cotopaxi.CotopaxiProperty.I18N_PROPERTY;
import static br.octahedron.cotopaxi.CotopaxiProperty.getProperty;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import br.octahedron.cotopaxi.CotopaxiProperty;
import br.octahedron.cotopaxi.controller.ControllerDescriptor;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.interceptor.TemplateInterceptor;
import br.octahedron.cotopaxi.view.response.TemplateResponse;

import static br.octahedron.util.DateUtil.defaultDateFormat;
/**
 * Interceptor for Internationalization. This Interceptor uses the {@link LocaleManager} to load the
 * right {@link LocaleMap} and inject it to template. The {@link LocaleMap} is bound to the
 * {@link CotopaxiProperty#I18N_PROPERTY}. You can access any value using
 * <em>i18n.get("RESOURCE_NAME")</em> (assuming I18N_PROPERTY default value).
 * 
 * It also binds a {@link DateFormat} and {@link NumberFormat} to output as
 * {@link CotopaxiProperty#I18N_DATE_FORMAT_PROPERTY} and
 * {@link CotopaxiProperty#I18N_NUMBER_FORMAT_PROPERTY} respectively.
 * 
 * By default it binds a {@link SimpleDateFormat} and {@link DecimalFormat} with no locale settings.
 * To use a custom Format overwrite the {@link I18NTemplateInterceptor#dateFormat(Locale)} and
 * {@link I18NTemplateInterceptor#numberFormat(Locale)} methods.
 * 
 * @see LocaleManager
 * @see LocaleMap
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class I18NTemplateInterceptor extends TemplateInterceptor {
	
	@Inject
	private LocaleManager localeManager;

	/**
	 * @param localeManager
	 *            the localeManager to set
	 */
	public void setLocaleManager(LocaleManager localeManager) {
		this.localeManager = localeManager;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void preRender(TemplateResponse response) {
		LocaleMap lcMap;
		ControllerDescriptor desc = this.controllerDescriptor();

		Locale lc = response.getLocale();
		if (lc != null) {
			lcMap = this.localeManager.getLocaleMap(desc, lc);
		} else {
			lcMap = this.localeManager.getLocaleMap(desc, this.locales());
		}

		response.addOutput(getProperty(I18N_PROPERTY), lcMap);
		response.addOutput(getProperty(I18N_DATE_FORMAT_PROPERTY), this.dateFormat(lcMap.getLocale()));
		response.addOutput(getProperty(I18N_NUMBER_FORMAT_PROPERTY), this.numberFormat(lcMap.getLocale()));
	}

	/**
	 * Gets the {@link DateFormat} for the given locale.
	 * 
	 * Override this method to provide custom {@link DateFormat} for given locale.
	 * 
	 * The default implementation returns a {@link SimpleDateFormat}.
	 * 
	 * @param lc
	 *            The locale to be used to render.
	 * @return The {@link DateFormat} to be injected to Template
	 */
	public DateFormat dateFormat(Locale lc) {
		return defaultDateFormat();
	}

	/**
	 * Gets the {@link NumberFormat} for the given locale.
	 * 
	 * Override this method to provide custom {@link NumberFormat} for given locale.
	 * 
	 * The default implementation returns a {@link DecimalFormat}.
	 * 
	 * @param lc
	 *            The locale to be used to render.
	 * @return The {@link NumberFormat} to be injected to Template
	 */
	public NumberFormat numberFormat(Locale lc) {
		return NumberFormat.getInstance();
	}

}
