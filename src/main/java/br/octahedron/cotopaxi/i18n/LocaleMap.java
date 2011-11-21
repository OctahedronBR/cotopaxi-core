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
 * TODO document
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class LocaleMap {
	
	private Collection<ResourceBundle> resources = new LinkedList<ResourceBundle>();
	private Locale locale;
	
	public LocaleMap(Locale locale) {
		this.locale = locale;
	}
	
	/**
	 * Gets this {@link LocaleMap} locale
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * TODO
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		for(ResourceBundle rsrc : this.resources) {
			if (rsrc.containsKey(key)) {
				return rsrc.getString(key);
			}
		}
		return null;
	}

	/**
	 * @param resource
	 */
	public void addResourceBundle(ResourceBundle resource) {
		if (resource!= null) { 
			this.resources.add(resource);
		}
	}
	
	public boolean isEmpty() {
		return this.resources.isEmpty();
	}
	
	protected int size() {
		return this.resources.size();
	}
}
