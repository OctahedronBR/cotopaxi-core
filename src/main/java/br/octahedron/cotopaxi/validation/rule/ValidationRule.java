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

/**
 * Defines the interface to validation rules. Validation Rules are responsible by check if a given
 * input is valid.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface ValidationRule {

	/**
	 * Checks if the given input is valid.
	 * 
	 * @param input the input to be validate
	 * @return <code>true</code> if the input is valid, <code>false</code> if it's not valid.
	 */
	public boolean isValid(String input);

}
