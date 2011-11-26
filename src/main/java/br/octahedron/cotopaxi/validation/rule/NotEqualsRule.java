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
package br.octahedron.cotopaxi.validation.rule;

import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.validation.Input;

/**
 * A {@link ComparableRule} that checks if an input is NOT EQUALS to a base value.
 * 
 * It uses the {@link Object#equals(Object)} to assert non-equality.
 * 
 * @see ComparableRule
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class NotEqualsRule extends EqualsRule {

	/**
	 * @see ComparableRule
	 */
	public NotEqualsRule(Input input, Converter<?> converter) {
		super(input, converter);
	}

	/**
	 * @see ComparableRule
	 */
	public NotEqualsRule(String message, Input input, Converter<?> converter) {
		super(message, input, converter);
	}

	/**
	 * @see ComparableRule
	 */
	public <T> NotEqualsRule(Converter<T> converter, T original) {
		super(converter, original);
	}

	/**
	 * @see ComparableRule
	 */
	public <T> NotEqualsRule(String message, Converter<T> converter, T original) {
		super(message, converter, original);
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected <T> boolean compare(T original, T other) {
		return !super.compare(original, other);
	}

}
