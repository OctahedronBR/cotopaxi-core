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
package br.octahedron.cotopaxi.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.octahedron.cotopaxi.controller.converter.BooleanConverter;
import br.octahedron.cotopaxi.controller.converter.DateConverter;
import br.octahedron.cotopaxi.controller.converter.NumberConverter;
import br.octahedron.cotopaxi.controller.converter.StringArrayConverter;
import br.octahedron.cotopaxi.controller.converter.NumberConverter.NumberType;

/**
 * Default interface for type converters. Converters are used to convert input data from
 * {@link String} to a given Type <code>T</code>
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface Converter<T> {

	/**
	 * Converts the given input {@link String}
	 * 
	 * @param input
	 *            the input as {@link String}
	 * 
	 * @return the converted input value
	 * 
	 * @throws ConvertionException
	 *             if input can't be converted
	 */
	public abstract T convert(String input) throws ConvertionException;

	/**
	 * Provide builder methods for built-in converters
	 * 
	 * You should use this methods to get converters instead of create a new instance directly.
	 */
	public static class Builder {

		// Cache structures
		@SuppressWarnings("unchecked")
		private static Map<NumberType, NumberConverter> numberConverter = new HashMap<NumberType, NumberConverter>();
		private static Map<String, StringArrayConverter> strArrayConverter = new HashMap<String, StringArrayConverter>();
		private static Map<String, DateConverter> dateConverters = new HashMap<String, DateConverter>();
		private static BooleanConverter bool = new BooleanConverter();

		/**
		 * Gets a {@link DateConverter} for the given {@link DateFormat}
		 * 
		 * @see SimpleDateFormat
		 * 
		 * @param dateFormat
		 *            the date format
		 * 
		 * @return the {@link DateConverter} for the given date format
		 */
		public static synchronized Converter<Date> date(String dateFormat) {
			if (!dateConverters.containsKey(dateFormat)) {
				dateConverters.put(dateFormat, new DateConverter(dateFormat));
			}
			return dateConverters.get(dateFormat);
		}

		public static Converter<Boolean> bool() {
			return bool;
		}

		/**
		 * A string array converter using <b>,</b> as separator
		 * 
		 * @return A string array converter using <b>,</b> as separator
		 */
		public static synchronized Converter<String[]> strArray(String separator) {
			if (!strArrayConverter.containsKey(separator)) {
				strArrayConverter.put(separator, new StringArrayConverter(separator));
			}
			return strArrayConverter.get(separator);
		}

		/**
		 * Gets a number Converter
		 * 
		 * @param numberType
		 *            The number type.
		 * @return A number converter for the given {@link NumberType}
		 */
		@SuppressWarnings("unchecked")
		public static synchronized Converter<? extends Number> number(NumberType numberType) {
			if (!numberConverter.containsKey(numberType)) {
				numberConverter.put(numberType, new NumberConverter(numberType));
			}
			return numberConverter.get(numberType);
		}

	}

}
