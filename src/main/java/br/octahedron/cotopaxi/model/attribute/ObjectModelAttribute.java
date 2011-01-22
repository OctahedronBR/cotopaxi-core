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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import br.octahedron.cotopaxi.InputHandler;
import br.octahedron.cotopaxi.model.attribute.converter.ConversionException;
import br.octahedron.util.reflect.ReflectionUtil;

/**
 * Represents a {@link ModelAttribute} that encapsulate other {@link ModelAttribute}s internally. It
 * useful to receive an fulfilled object as parameter instead of receive each attribute separately.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ObjectModelAttribute<T> implements ModelAttribute<T> {

	private Collection<ModelAttribute<?>> internalAtts = new LinkedList<ModelAttribute<?>>();
	private Class<T> klass;
	private String name;
	

	public ObjectModelAttribute(Class<T> klass) {
		this.klass = klass;
		this.name = klass.getSimpleName().toLowerCase();
	}

	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Adds a {@link ModelAttribute} to this {@link ObjectModelAttribute}.
	 * 
	 * @param internalAttribute
	 *            the {@link ModelAttribute} to be added
	 */
	public void addAttribute(ModelAttribute<?> internalAttribute) {
		this.internalAtts.add(internalAttribute);
	}

	@Override
	public T getAttributeValue(InputHandler input) throws InvalidAttributeException, ConversionException {
		try {
			T instance = (T) this.klass.newInstance();
			for (ModelAttribute<?> att : this.internalAtts) {
				Object value = att.getAttributeValue(input);
				Method met = ReflectionUtil.getSetMethod(att.getName(), this.klass, value.getClass());
				ReflectionUtil.invoke(instance, met, value);
			}
			return instance;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InvalidAttributeException(ex);
		}
	}

}
