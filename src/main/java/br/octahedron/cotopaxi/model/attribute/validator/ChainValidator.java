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
package br.octahedron.cotopaxi.model.attribute.validator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A validator that only delivers the validation task to other validator, make possible to use as
 * many as desired validators to an unique field.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class ChainValidator<T> implements Validator<T> {

	private List<Validator<T>> validators;

	public ChainValidator() {

	}

	/**
	 * Creates an ChainValidator using the given validators.
	 */
	public ChainValidator(Validator<T>... validators) {
		this.validators = new LinkedList<Validator<T>>();
		this.validators.addAll(Arrays.asList(validators));
	}

	/**
	 * Adds a validator to this ChainValidator.
	 * 
	 * @param validator
	 *            the validator to be added.
	 */
	public void addValidator(Validator<T> validator) {
		if (this.validators == null) {
			this.validators = new LinkedList<Validator<T>>();
		}
		this.validators.add(validator);
	}

	@Override
	public boolean isValid(T object) {
		for (Validator<T> validator : this.validators) {
			boolean result = validator.isValid(object);
			if (!result) {
				return false;
			}
		}
		return true;
	}

}
