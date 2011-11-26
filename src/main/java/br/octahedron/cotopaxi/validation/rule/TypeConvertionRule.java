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

/**
 * This rule checks if a input is convertible using the given converter. If the converter returns
 * null, it means, it's not able to convert input, it considers the input as invalid.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class TypeConvertionRule extends AbstractRule {

	private Converter<?> converter;

	/**
	 * @param message
	 *            The message to be shown if validation fails.
	 * @param converter
	 *            The converter to be used to check if the input is valid.
	 */
	public <T> TypeConvertionRule(String message, Converter<T> converter) {
		super(message);
		this.converter = converter;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public boolean isValid(String input) {
		return this.converter.convert(input) != null;
	}

}
