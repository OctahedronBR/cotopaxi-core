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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A concrete subclass of <code>ResourceBundle</code> that manages resources for a locale using a
 * map.
 * 
 * @see ResourceBundle
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class MapResourceBundle extends ResourceBundle implements Serializable {

	private static final long serialVersionUID = 8707042123171284167L;

	private Map<String, String> map;

	public MapResourceBundle(Map<String, String> map) {
		this.map = map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ResourceBundle#getKeys()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Enumeration getKeys() {
		return new IteratorEnumeration(this.map.keySet().iterator());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	 */
	@Override
	protected Object handleGetObject(String key) {
		Object value = this.map.get(key);
		if (key == null) {
			throw new NullPointerException();
		}
		if (value == null) {
			return key;
		}

		return this.map.get(key);
	}

	private class IteratorEnumeration<T> implements Enumeration<T> {

		private Iterator<T> iterator;

		public IteratorEnumeration(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		public boolean hasMoreElements() {
			return this.iterator.hasNext();
		}

		public T nextElement() {
			return this.iterator.next();
		}

	}
}
