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
package br.octahedron.cotopaxi.model;

import java.util.Collection;
import java.util.LinkedList;

import br.octahedron.cotopaxi.model.attribute.ModelAttribute;
import br.octahedron.cotopaxi.model.attribute.RequestModelAttribute;
import br.octahedron.cotopaxi.model.attribute.SessionModelAttribute;
import br.octahedron.cotopaxi.model.attribute.converter.TypeConverter;
import br.octahedron.cotopaxi.model.attribute.validator.Validator;

/**
 * An adapter to provides to controller the necessary information about the input parameters
 * necessary to execute an Action.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class InputAdapter {

	private Collection<ModelAttribute<?>> attributes;

	public boolean hasAttributes() {
		return this.attributes != null;
	}

	/**
	 * @return A Collection with all attributes in <i>Insertion Order</i>, or null if doesn't exists
	 *         any attribute.
	 */
	public Collection<ModelAttribute<?>> getAttributes() {
		return this.attributes;
	}

	/**
	 * TODO DOCUMENT
	 * 
	 * @param attribute
	 */
	public void addAttribute(ModelAttribute<?> attribute) {
		if (this.attributes == null) {
			this.attributes = new LinkedList<ModelAttribute<?>>();
		}
		this.attributes.add(attribute);
	}

	/**
	 * TODO DOCUMENT
	 * 
	 * @param <T>
	 * @param name
	 * @param typeConverter
	 */
	public <T> void addAttribute(String name, Class<? extends TypeConverter<T>> typeConverter) {
		this.addAttribute(new RequestModelAttribute<T>(name, typeConverter));
	}

	/**
	 * TODO DOCUMENT
	 * 
	 * @param <T>
	 * @param name
	 * @param typeConverter
	 * @param validator
	 */
	public <T> void addAttribute(String name, Class<? extends TypeConverter<T>> typeConverter, Validator<T> validator) {
		this.addAttribute(new RequestModelAttribute<T>(name, typeConverter, validator));
	}

	/**
	 * TODO DOCUMENT
	 * 
	 * @param <T>
	 * @param name
	 * @param klass
	 */
	public <T> void addSessionAttribute(String name, Class<T> klass) {
		this.addAttribute(new SessionModelAttribute<T>(name));
	}
}
