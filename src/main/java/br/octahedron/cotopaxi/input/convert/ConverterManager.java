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
package br.octahedron.cotopaxi.input.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.inject.InstanceHandler;

/**
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ConverterManager {

	private static final Logger logger = Logger.getLogger(ConverterManager.class.getName());
	private static final ConverterManager instance = new ConverterManager();

	/**
	 * @return The unique {@link ConverterManager} instance
	 */
	public static ConverterManager getInstance() {
		return instance;
	}

	private Map<Class<?>, Class<? extends TypeConverter<?>>> converters;

	private ConverterManager() {
		this.converters = new HashMap<Class<?>, Class<? extends TypeConverter<?>>>();
		this.registerDefaults();
	}

	/**
	 * 
	 */
	private void registerDefaults() {
		this.registerConverter(String.class, StringConverter.class);
		this.registerConverter(Character.class, CharacterConverter.class);
		this.registerConverter(Byte.class, ByteConverter.class);
		this.registerConverter(Boolean.class, BooleanConverter.class);
		this.registerConverter(Double.class, DoubleConverter.class);
		this.registerConverter(Float.class, FloatConverter.class);
		this.registerConverter(Integer.class, IntegerConverter.class);
		this.registerConverter(Long.class, LongConverter.class);
		this.registerConverter(Short.class, ShortConverter.class);
		this.registerConverter(Date.class, DateConverter.class);
		this.registerConverter(URL.class, URLConverter.class);
		this.registerConverter(BigDecimal.class, BigDecimalConverter.class);
		this.registerConverter(BigInteger.class, BigIntegerConverter.class);
	}

	/**
	 * Registers and converter for a given type. The given converter will be used by the framework
	 * to convert Objects to the given targetClass.
	 * 
	 * If already exists a converter for the given targetClass, it will be overwritten.
	 */
	public void registerConverter(Class<?> targetClass, Class<? extends TypeConverter<?>> converter) {
		if (targetClass != null) {
			logger.fine("Registring " + converter.getName() + " as converter for class " + targetClass.getName());
			this.converters.put(targetClass, converter);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T convert(Class<T> toClass, String strValue) throws ConversionException {
		if (this.converters.containsKey(toClass)) {
			Class<? extends TypeConverter<?>> converterClass = this.converters.get(toClass);
			try {
				TypeConverter<T> converter = (TypeConverter<T>) InstanceHandler.getInstance(converterClass);
				return converter.convert(strValue);
			} catch (InstantiationException ex) {
				logger.log(Level.SEVERE, "Unable to create converter " + converterClass.getName(), ex);
				throw new RuntimeException("Unable to create converter " + converterClass.getName(), ex);
			}
		}
		return null;
	}
}
