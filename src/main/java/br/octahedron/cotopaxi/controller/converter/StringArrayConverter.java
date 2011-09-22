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

import java.util.regex.PatternSyntaxException;

import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.controller.ConvertionException;

/**
 * Converts a String input to String array, using a given separator  
 * 
 * @see String#split(String)
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class StringArrayConverter implements Converter<String[]> {

	private String separator;

	/**
	 * Creates a {@link StringArrayConverter} using <b>,</b> as separator.
	 */
	public StringArrayConverter() {
		this(",");
	}

	/**
	 * Creates a {@link StringArrayConverter} using the given separator
	 */
	public StringArrayConverter(String separator) {
		this.separator = separator;
	}

	@Override
	public String[] convert(String input) throws ConvertionException {
		try {
			return input.split(this.separator);
		} catch (PatternSyntaxException ex) {
			throw new ConvertionException(ex);
		}
	}

}
