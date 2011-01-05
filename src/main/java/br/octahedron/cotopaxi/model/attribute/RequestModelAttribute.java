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
import br.octahedron.cotopaxi.model.attribute.converter.TypeConverter;
import br.octahedron.cotopaxi.model.attribute.validator.Validator;
import br.octahedron.util.reflect.InstanceHandler;

/**
 * This entity represents a Model Attribute. It used by the Cotopaxi to convert and validate input
 * attributes.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 * @param <T>
 */
public class RequestModelAttribute<T> implements ModelAttribute<T> {

	private static InstanceHandler<TypeConverter<?>> converters = new InstanceHandler<TypeConverter<?>>();

	private String name;
	private Class<? extends TypeConverter<T>> typeConverter;
	private Validator<T> validator;

	public RequestModelAttribute(String name, Class<? extends TypeConverter<T>> typeConverter) {
		this.name = name;
		this.typeConverter = typeConverter;
	}

	public RequestModelAttribute(String name, Class<? extends TypeConverter<T>> typeConverter, Validator<T> validator) {
		this(name, typeConverter);
		this.validator = validator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.model.attribute.ModelAttribute#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.model.attribute.ModelAttribute#getAttributeValue(br.octahedron.cotopaxi
	 * .model.attribute.InputHandler)
	 */
	@SuppressWarnings("unchecked")
	public T getAttributeValue(InputHandler input) throws InvalidAttributeException, ConversionException {
		String strAttValue = input.getRequestParameter(this.name);

		// try to convert
		TypeConverter<T> converter = (TypeConverter<T>) converters.getInstance(this.typeConverter);
		T converted = converter.convert(strAttValue);
		// check if there's a validator register for the att
		if (this.validator != null) {
			// try validate
			Validator<T> validator = this.validator;
			if (validator.isValid(converted)) {
				// that's okay, add it to params!
				return converted;
			} else {
				throw new InvalidAttributeException();
			}
		} else {
			// if there isn't a validator, just add the converted att to params list
			return converted;
		}
	}
}