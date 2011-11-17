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

import static br.octahedron.cotopaxi.inject.Injector.getInstance;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.octahedron.cotopaxi.controller.converter.BigDecimalConverter;
import br.octahedron.cotopaxi.controller.converter.BigIntegerConverter;
import br.octahedron.cotopaxi.controller.converter.BooleanConverter;
import br.octahedron.cotopaxi.controller.converter.ByteConverter;
import br.octahedron.cotopaxi.controller.converter.DateConverter;
import br.octahedron.cotopaxi.controller.converter.DoubleConverter;
import br.octahedron.cotopaxi.controller.converter.FloatConverter;
import br.octahedron.cotopaxi.controller.converter.IntegerConverter;
import br.octahedron.cotopaxi.controller.converter.LongConverter;
import br.octahedron.cotopaxi.controller.converter.SafeStringConverter;
import br.octahedron.cotopaxi.controller.converter.ShortConverter;
import br.octahedron.cotopaxi.controller.converter.StringArrayConverter;
import br.octahedron.cotopaxi.controller.converter.StringConverter;

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
		private static Map<String, StringArrayConverter> strArrayConverter = new HashMap<String, StringArrayConverter>();
		private static Map<String, DateConverter> dateConverters = new HashMap<String, DateConverter>();
		private static Converter<String> rawStringConverter;
		private static Converter<String> safeStringConverter;

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

		/**
		 * Gets a {@link BooleanConverter}.
		 * 
		 * @return a {@link BooleanConverter}
		 */
		public static Converter<Boolean> bool() {
			try {
				return getInstance(BooleanConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
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
		 * Gets a {@link StringConverter}
		 * 
		 * @return a {@link StringConverter}
		 */
		public static synchronized Converter<String> string() {
			if (rawStringConverter == null) {
				rawStringConverter = new StringConverter();
			}
			return rawStringConverter;
		}

		/**
		 * Gets a {@link SafeStringConverter}. This kind of converters strips any html code from
		 * input.
		 * 
		 * @return a {@link SafeStringConverter}
		 */
		public static synchronized Converter<String> safeString() {
			if (safeStringConverter == null) {
				safeStringConverter = new SafeStringConverter();
			}
			return safeStringConverter;
		}

		/**
		 * Gets a ByteConverter
		 * 
		 * @return A byte converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<Byte> byteNumber() {
			try {
				return getInstance(ByteConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a ShortConverter
		 * 
		 * @return A short converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<Short> shortNumber() {
			try {
				return getInstance(ShortConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a IntegerConverter
		 * 
		 * @return A int converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<Integer> intNumber() {
			try {
				return getInstance(IntegerConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a LongConverter
		 * 
		 * @return A long converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<Long> longNumber() {
			try {
				return getInstance(LongConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a FloatConverter
		 * 
		 * @return A float converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<Float> floatNumber() {
			try {
				return getInstance(FloatConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a DoubleConverter
		 * 
		 * @return A double converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<Double> doubleNumber() {
			try {
				return getInstance(DoubleConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a BigIntegerConverter
		 * 
		 * @return A BigInteger converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<BigInteger> bigIntNumber() {
			try {
				return getInstance(BigIntegerConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 * Gets a BigDecimalConverter
		 * 
		 * @return A BigDecimal converter
		 */
		@SuppressWarnings("unchecked")
		public static Converter<BigDecimal> bigDecimalNumber() {
			try {
				return getInstance(BigDecimalConverter.class);
			} catch (InstantiationException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

}
