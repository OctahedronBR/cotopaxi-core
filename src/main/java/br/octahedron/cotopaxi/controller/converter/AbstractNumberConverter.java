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
package br.octahedron.cotopaxi.controller.converter;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.controller.ConvertionException;

/**
 * Base class for {@link Converter}s for basic types.
 * 
 * It converts string to {@link BigDecimal}, {@link BigInteger}, {@link Byte}, {@link Double},
 * {@link Float}, {@link Integer}, {@link Long} and {@link Short}.
 * 
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractNumberConverter<T extends Number> implements Converter {

	private Class<T> klass;

	/**
	 * Creates a number converter for the given {@link NumberType}.
	 * 
	 * 
	 * @param numberType
	 *            The {@link NumberType}.
	 */
	public AbstractNumberConverter(Class<T> klass) {
		this.klass = klass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.controller.Converter#convert(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T convert(String input) throws ConvertionException {
		try {
			Constructor<? extends Number> constructor = this.klass.getConstructor(String.class);
			return (T) constructor.newInstance(input);
		} catch (Exception ex) {
			throw new ConvertionException(ex);
		}
	}
}
