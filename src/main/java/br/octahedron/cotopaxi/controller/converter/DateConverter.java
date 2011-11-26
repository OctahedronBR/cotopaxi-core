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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.octahedron.cotopaxi.controller.Converter;

/**
 * A converter for {@link Date}
 * 
 * @see Date
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class DateConverter implements Converter<Date> {

	private SimpleDateFormat formatter;

	/**
	 * Creates a new {@link DateConverter} using the given date format
	 * 
	 * @see SimpleDateFormat
	 */
	public DateConverter(String dateFormat) {
		this.formatter = new SimpleDateFormat(dateFormat);
	}

	@Override
	public Date convert(String input) {
		try {
			return this.formatter.parse(input);
		} catch (ParseException ex) {
			return null;
		}

	}
}
