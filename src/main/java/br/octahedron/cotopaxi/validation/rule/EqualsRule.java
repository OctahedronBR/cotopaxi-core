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
 * A {@link ComparableRule} that checks if an input is EQUALS to a base value.
 * 
 * It uses the <code>equals(Object)</code> method to assert equality.
 * 
 * @see ComparableRule
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class EqualsRule extends ComparableRule {

	/**
	 * @see ComparableRule
	 */
	public EqualsRule(Input input, Converter<?> converter) {
		super(input, converter);
	}

	/**
	 * @see ComparableRule
	 */
	public EqualsRule(String message, Input input, Converter<?> converter) {
		super(message, input, converter);
	}

	/**
	 * @see ComparableRule
	 */
	public <T> EqualsRule(Converter<T> converter, T original) {
		super(converter, original);
	}

	/**
	 * @see ComparableRule
	 */
	public <T> EqualsRule(String message, Converter<T> converter, T original) {
		super(message, converter, original);
	}

	/**
	 * Compares the given objects using the {@link Object#equals(Object)} method.
	 * 
	 * @see ComparableRule#compare(Object, Object)
	 */
	@Override
	protected <T> boolean compare(T original, T other) {
		return original.equals(other);
	}
}
