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

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.inject.SelfInjectable;

/**
 * A helper class to be used by controllers to retrieve i18n messages.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ControllerI18nHelper extends SelfInjectable {

	@Inject
	private LocaleManager localeManager;
	private Controller controller;
	private Map<Locale, LocaleMap> cache = new HashMap<Locale, LocaleMap>();

	/**
	 * Creates a {@link ControllerI18nHelper} for the given {@link Controller}
	 * 
	 * @see LocaleManager#getLocaleMap(Controller, Locale)
	 * 
	 * @param controller
	 *            The controller which this helper refers to.
	 */
	public ControllerI18nHelper(Controller controller) {
		this.controller = controller;
	}

	/**
	 * @param localeManager
	 *            the localeManager to set
	 */
	public void setLocaleManager(LocaleManager localeManager) {
		this.localeManager = localeManager;
	}

	/**
	 * Gets the given i18n message for the given locales.
	 * 
	 * It will first determine which is the most appropriate locale to use.
	 * 
	 * @param lcs
	 *            The possible locales list, order by priority
	 * @param message
	 *            The message to be retrieved.
	 * 
	 * @return The message, translated to given locale or <code>null</code> if there's no such
	 *         message
	 */
	public String get(Collection<Locale> lcs, String message) {
		Locale lc = this.localeManager.findLocale(lcs);
		LocaleMap map = this.getLocaleMap(lc);
		return map.get(message);
	}

	/**
	 * Gets the given i18n message for the given locale.
	 * 
	 * @param lc
	 *            The locale to be get the message.
	 * @param message
	 *            The message to be retrieved.
	 * 
	 * @return The message, translated to given locale or <code>null</code> if there's no such
	 *         message
	 */
	public String get(Locale lc, String message) {
		LocaleMap map = this.getLocaleMap(lc);
		return map.get(message);
	}

	/**
	 * Gets the {@link LocaleMap} for the given Locale
	 */
	private synchronized LocaleMap getLocaleMap(Locale lc) {
		if (!this.cache.containsKey(lc)) {
			this.cache.put(lc, this.localeManager.getLocaleMap(this.controller, lc));
		}
		return this.cache.get(lc);
	}
}
