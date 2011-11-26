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
 * A {@link ComparableRule} that checks if an input is GREATER THAN the base value.
 * 
 * It assumes that the type being compared implements the {@link Comparable} interface.
 * 
 * @see ComparableRule
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class GreaterThanRule extends ComparableRule {

	/**
	 * @see ComparableRule
	 */
	public GreaterThanRule(Input baseInput, Converter<?> converter) {
		super(baseInput, converter);
	}

	/**
	 * @see ComparableRule
	 */
	public GreaterThanRule(String message, Input baseInput, Converter<?> converter) {
		super(message, baseInput, converter);
	}

	/**
	 * @see ComparableRule
	 */
	public <T> GreaterThanRule(Converter<T> converter, T baseLiteral) {
		super(converter, baseLiteral);
	}

	/**
	 * @see ComparableRule
	 */
	public <T> GreaterThanRule(String message, Converter<T> converter, T baseLiteral) {
		super(message, converter, baseLiteral);
	}

	/*
	 * (non-Javadoc)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <T> boolean compare(T base, T other) {
		try {
			Comparable<T> compareBase = (Comparable<T>) base;
			return compareBase.compareTo((T) other) < 0;
		} catch (ClassCastException ex) {
			return false;
		}
	}

}
