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
package br.octahedron.cotopaxi.validation;

import java.util.regex.Pattern;

import br.octahedron.cotopaxi.validation.rule.LengthRule;
import br.octahedron.cotopaxi.validation.rule.MaximumLengthRule;
import br.octahedron.cotopaxi.validation.rule.MinimumLengthRule;
import br.octahedron.cotopaxi.validation.rule.RegexRule;
import br.octahedron.cotopaxi.validation.rule.RequiredRule;
import br.octahedron.cotopaxi.validation.rule.RegexRule.CommonPattern;

/**
 * Defines the base for validation rules. Validation Rules are responsible by check if a given
 * input is valid.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class Rule {
	
	protected String message;

	public Rule(String message) {
		this.message = message;
	}

	/**
	 * Gets the message for validation fail
	 * 
	 * @return The message to validation fail
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Checks if the given input is valid.
	 * 
	 * @param input
	 *            the input to be validate
	 * @return <code>true</code> if the input is valid, <code>false</code> if it's not valid.
	 */
	public abstract boolean isValid(String input);

	/**
	 * A builder for basic validation rules and input
	 */
	public static class Builder {

		public static Input literal(String literal) {
			return new Input.LiteralInput(literal);
		}

		public static Input attribute(String attributeName) {
			return new Input.AttributeInput(attributeName);
		}
		
		public static Input session(String attributeName) {
			return new Input.SessionInput(attributeName);
		}
		
		public static Input header(String headerName) {
			return new Input.HeaderInput(headerName);
		}
		
		public static Input cookie(String cookieName) {
			return new Input.AttributeInput(cookieName);
		}

		public static Rule required() {
			return new RequiredRule(null);
		}

		public static Rule required(String message) {
			return new RequiredRule(message);
		}

		public static Rule minLength(int min) {
			return new MinimumLengthRule(null, min);
		}

		public static Rule minLength(int min, String message) {
			return new MinimumLengthRule(message, min);
		}

		public static Rule maxLength(int max) {
			return new MaximumLengthRule(null, max);
		}

		public static Rule maxLength(int max, String message) {
			return new MaximumLengthRule(message, max);
		}

		public static Rule length(int min, int max) {
			return new LengthRule(null, min, max);
		}

		public static Rule length(int min, int max, String message) {
			return new LengthRule(message, min, max);
		}

		public static Rule regex(CommonPattern pattern) {
			return new RegexRule(null, pattern);
		}

		public static Rule regex(CommonPattern pattern, String message) {
			return new RegexRule(message, pattern);
		}

		public static Rule regex(String pattern) {
			return new RegexRule(null, pattern);
		}

		public static Rule regex(String pattern, String message) {
			return new RegexRule(message, pattern);
		}
		
		public static Rule regex(Pattern pattern) {
			return new RegexRule(null, pattern);
		}

		public static Rule regex(Pattern pattern, String message) {
			return new RegexRule(message, pattern);
		}
		
		/*
		 * equals
		 * 
		 * greater than
		 * 
		 * lesser than
		 * 
		 * range
		 */

	}
}
