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

import br.octahedron.cotopaxi.model.attribute.converter.TypeConverter;
import br.octahedron.cotopaxi.model.attribute.validator.Validator;
import br.octahedron.util.reflect.ReflectionUtil;

/**
 * This entity represents a Model Attribute. It used by the Cotopaxi to convert and validate input
 * attributes.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 * @param <T>
 */
public class ModelAttribute<T> {
	private String name;
	private String defaultValue;
	private Class<? extends TypeConverter<T>> typeConverter;
	private Validator<T> validator;

	public ModelAttribute(String name, Class<? extends TypeConverter<T>> typeConverter) {
		this.name = name;
		this.typeConverter = typeConverter;
	}

	public ModelAttribute(String name, Class<? extends TypeConverter<T>> typeConverter, String defaultValue) {
		this(name, typeConverter);
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}
	}

	public ModelAttribute(String name, Class<? extends TypeConverter<T>> typeConverter, Validator<T> validator, String defaultValue) {
		this(name, typeConverter, defaultValue);
		this.validator = validator;
	}

	public ModelAttribute(String name, Class<? extends TypeConverter<T>> typeConverter, Validator<T> validator) {
		this(name, typeConverter, validator, null);
	}

	/**
	 * Returns the attribute's name. The name identifies the attribute at the request's parameters.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the default value for this attribute. If not defined at constructor it uses the default
	 * values defined by the {@link ReflectionUtil#getDefaultValue(Class)}
	 * 
	 * @return
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return the type converter class
	 */
	public Class<? extends TypeConverter<T>> getTypeConverterClass() {
		return this.typeConverter;
	}

	/**
	 * @return <code>true</code> if this ModelAttribute has a validator, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasValidator() {
		return this.validator != null;
	}

	/**
	 * @return the validator class
	 */
	public Validator<T> getValidator() {
		return this.validator;
	}
}