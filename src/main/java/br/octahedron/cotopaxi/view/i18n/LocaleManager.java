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
package br.octahedron.cotopaxi.view.i18n;

import java.util.Locale;

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.util.ThreadProperties;

/**
 * @author nome - email@octahedron.com.br
 *
 */
public class LocaleManager {
	
	private static final String LOCALE_KEY = "user_locale";
	private static final LocaleManager instance = new LocaleManager();
	
	public static LocaleManager getInstance() {
		return instance;
	}

	private LocaleManager() {
	}

	public Locale getLocale(RequestWrapper request) {
		if ( request.isLocaleFromURL() ) {
			// if locale came from URL, set it to session for next requests
			Locale lc = request.getLocale();
			ThreadProperties.setProperty(LOCALE_KEY, lc);
			return lc;
		} else {
			if ( ThreadProperties.containsProperty(LOCALE_KEY) ) {
				// check if there's a previous configured locale
				return (Locale) ThreadProperties.getProperty(LOCALE_KEY);
			} else {
				// once there's no locale on session, return requested 
				return request.getLocale();
			}
		}
	}

}
