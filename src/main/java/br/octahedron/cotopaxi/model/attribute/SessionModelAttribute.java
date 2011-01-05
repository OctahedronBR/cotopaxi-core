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
package br.octahedron.cotopaxi.model.attribute;

import br.octahedron.cotopaxi.InputHandler;
import br.octahedron.cotopaxi.model.attribute.converter.ConversionException;

/**
 * @author Name - email@octahedron.com.br
 * @param <T>
 * 
 */
public class SessionModelAttribute<T> implements ModelAttribute<T> {

	private String name;

	public SessionModelAttribute(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getAttributeValue(InputHandler input) throws InvalidAttributeException, ConversionException {
		try {
			T value = (T) input.getSessionParameter(this.name);
			if (value == null) {
				throw new InvalidAttributeException();
			}
			return value;
		} catch (ClassCastException ex) {
			throw new ConversionException(ex);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

}
