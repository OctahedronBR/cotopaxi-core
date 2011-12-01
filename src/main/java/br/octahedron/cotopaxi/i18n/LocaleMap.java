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
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class provides access to i18n messages for a given locale. It loads a set of
 * {@link ResourceBundle} to retrieve messages.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class LocaleMap {

	private Collection<ResourceBundle> resources = new LinkedList<ResourceBundle>();
	private Locale locale;

	public LocaleMap(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Gets this {@link LocaleMap} locale
	 * 
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Gets the given i18n message translated. It lookup at different {@link ResourceBundle}, by the
	 * inverted insertion order, and stops on the first occurrence.
	 * 
	 * @param key
	 *            The message to be retrieved.
	 * 
	 * @return The message, translated to given locale or <code>null</code> if there's no such
	 *         message
	 */
	public String get(String key) {
		for (ResourceBundle rsrc : this.resources) {
			if (rsrc.containsKey(key)) {
				return rsrc.getString(key);
			}
		}
		return null;
	}

	/**
	 * Checks if this {@link LocaleMap} has no {@link ResourceBundle} 
	 * @return <code>true</code> if there no {@link ResourceBundle} to be used, <code>false</code> otherwise.
	 */
	public boolean isEmpty() {
		return this.resources.isEmpty();
	}

	/**
	 * @param resource
	 */
	protected void addResourceBundle(ResourceBundle resource) {
		if (resource != null) {
			this.resources.add(resource);
		}
	}

	protected int size() {
		return this.resources.size();
	}
}
