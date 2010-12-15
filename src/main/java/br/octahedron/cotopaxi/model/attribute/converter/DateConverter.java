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
package br.octahedron.cotopaxi.model.attribute.converter;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts a string to a {@link Date}. It uses a {@link DateFormat} to parse the String. A new
 * format can be setted using the methods {@link DateConverter#setDateFormat(DateFormat)} or
 * {@link DateConverter#setDateFormat(String)}. If no DateFormat is given it uses the default
 * DateFormat instance with the default formatting style for the default locale.
 * 
 * @author Danilo Penna Queiroz - email@octahedron.com.br
 */
public class DateConverter implements TypeConverter<Date> {

	private static DateFormat formatter = DateFormat.getInstance();

	public static void setDateFormat(DateFormat newFormat) {
		formatter = newFormat;
	}

	/**
	 * @param strDateFormat
	 * @see SimpleDateFormat#SimpleDateFormat(String)
	 */
	public static void setDateFormat(String strDateFormat) {
		formatter = new SimpleDateFormat(strDateFormat);
	}

	@Override
	public Date convert(String strValue) throws ConversionException {
		if (strValue != null) {
			Date result = formatter.parse(strValue, new ParsePosition(0));
			if (result == null) {
				throw new ConversionException("Unable to parse date.");
			} else {
				return result;
			}
		} else {
			throw new ConversionException("String is null");
		}
	}
}
