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

import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.validation.rule.EqualsRule;
import br.octahedron.cotopaxi.validation.rule.GreaterThanRule;
import br.octahedron.cotopaxi.validation.rule.LengthRule;
import br.octahedron.cotopaxi.validation.rule.LessThanRule;
import br.octahedron.cotopaxi.validation.rule.MaximumLengthRule;
import br.octahedron.cotopaxi.validation.rule.MinimumLengthRule;
import br.octahedron.cotopaxi.validation.rule.NotEqualsRule;
import br.octahedron.cotopaxi.validation.rule.RegexRule;
import br.octahedron.cotopaxi.validation.rule.RequiredRule;
import br.octahedron.cotopaxi.validation.rule.RegexRule.CommonPattern;
import br.octahedron.cotopaxi.validation.rule.TypeConvertionRule;

/**
 * The interface for validation Rules.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface Rule {

	/**
	 * Gets the message for validation fail
	 * 
	 * @return The message to validation fail
	 */
	public abstract String getMessage();

	/**
	 * Checks if the given input is valid.
	 * 
	 * @param input
	 *            the input to be validate
	 * @return <code>true</code> if the input is valid, <code>false</code> if it's not valid.
	 */
	public abstract boolean isValid(String input);

	/**
	 * A builder for basic validation rules
	 */
	public static class Builder {
		
		/**
		 * Checks input type by using a converter
		 * 
		 */
		public static <T> Rule type(Converter<T> converter) {
			return type(null, converter);
		}

		/**
		 * Checks input type by using a converter
		 * 
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static <T> Rule type(String message, Converter<T> converter) {
			return new TypeConvertionRule(message, converter);
		}

		/**
		 * Indicates a input is required
		 */
		public static Rule required() {
			return required(null);
		}

		/**
		 * Indicates a input is required
		 * 
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule required(String message) {
			return new RequiredRule(message);
		}

		/**
		 * Indicates a input minimum length, inclusive.
		 * 
		 * @param min
		 *            the minimum length, inclusive.
		 */
		public static Rule minLength(int min) {
			return minLength(min, null);
		}

		/**
		 * Indicates a input minimum length, inclusive.
		 * 
		 * @param min
		 *            the minimum length, inclusive.
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule minLength(int min, String message) {
			return new MinimumLengthRule(message, min);
		}

		/**
		 * Indicates a input maximum length, inclusive.
		 * 
		 * @param min
		 *            the maximum length, inclusive.
		 */
		public static Rule maxLength(int max) {
			return maxLength(max, null);
		}

		/**
		 * Indicates a input maximum length, inclusive.
		 * 
		 * @param min
		 *            the maximum length, inclusive.
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule maxLength(int max, String message) {
			return new MaximumLengthRule(message, max);
		}

		/**
		 * Indicates a input minimum and maximum length
		 * 
		 * @param min
		 *            the minimum length, inclusive.
		 * @param max
		 *            the maximum length, inclusive
		 */
		public static Rule length(int min, int max) {
			return length(min, max, null);
		}

		/**
		 * Indicates a input minimum and maximum length
		 * 
		 * @param min
		 *            the minimum length, inclusive.
		 * @param max
		 *            the maximum length, inclusive
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule length(int min, int max, String message) {
			return new LengthRule(message, min, max);
		}

		/**
		 * Indicates input should match the given regex
		 * 
		 * @param pattern
		 *            The input regex
		 * @see Pattern
		 */
		public static Rule regex(CommonPattern pattern) {
			return regex(pattern, null);
		}

		/**
		 * Indicates input should match the given regex
		 * 
		 * @param pattern
		 *            The input regex
		 * @param message
		 *            The message to be shown if validation fails
		 * @see Pattern
		 */
		public static Rule regex(CommonPattern pattern, String message) {
			return new RegexRule(message, pattern);
		}

		/**
		 * Indicates input should match the given regex
		 * 
		 * @param pattern
		 *            The input regex
		 * @see Pattern
		 */
		public static Rule regex(String pattern) {
			return regex(pattern, null);
		}

		/**
		 * Indicates input should match the given regex
		 * 
		 * @param pattern
		 *            The input regex
		 * @param message
		 *            The message to be shown if validation fails
		 * @see Pattern
		 */
		public static Rule regex(String pattern, String message) {
			return new RegexRule(message, pattern);
		}

		/**
		 * Indicates input should match the given regex
		 * 
		 * @param pattern
		 *            The input regex
		 * @see Pattern
		 */
		public static Rule regex(Pattern pattern) {
			return regex(pattern, null);
		}

		/**
		 * Indicates input should match the given regex
		 * 
		 * @param pattern
		 *            The input regex
		 * @param message
		 *            The message to be shown if validation fails
		 * @see Pattern
		 */
		public static Rule regex(Pattern pattern, String message) {
			return new RegexRule(message, pattern);
		}

		/**
		 * Indicates another input should be EQUALS to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 */
		public static Rule equalsTo(Input input, Converter<?> converter) {
			return equalsTo(input, converter, null);
		}

		/**
		 * Indicates another input should be EQUALS to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule equalsTo(Input input, Converter<?> converter, String message) {
			return new EqualsRule(message, input, converter);
		}

		/**
		 * Indicates another input should be EQUALS to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 */
		public static <T> Rule equalsTo(Converter<T> converter, T base) {
			return equalsTo(converter, base, null);
		}

		/**
		 * Indicates another input should be EQUALS to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static <T> Rule equalsTo(Converter<T> converter, T base, String message) {
			return new EqualsRule(message, converter, base);
		}

		/**
		 * Indicates another input should be NOT EQUALS to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 */
		public static Rule notEquals(Input input, Converter<?> converter) {
			return notEquals(input, converter, null);
		}

		/**
		 * Indicates another input should be NOT EQUALS to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule notEquals(Input input, Converter<?> converter, String message) {
			return new NotEqualsRule(message, input, converter);
		}

		/**
		 * Indicates another input should be NOT EQUALS to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 */
		public static <T> Rule notEquals(Converter<T> converter, T base) {
			return notEquals(converter, base, null);
		}

		/**
		 * Indicates another input should be NOT EQUALS to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static <T> Rule notEquals(Converter<T> converter, T base, String message) {
			return new NotEqualsRule(message, converter, base);
		}

		/**
		 * Indicates another input should be GREATER THAN to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 */
		public static Rule greaterThan(Input input, Converter<?> converter) {
			return greaterThan(input, converter, null);
		}

		/**
		 * Indicates another input should be GREATER THAN to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule greaterThan(Input input, Converter<?> converter, String message) {
			return new GreaterThanRule(message, input, converter);
		}

		/**
		 * Indicates another input should be GREATER THAN to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 */
		public static <T> Rule greaterThan(Converter<T> converter, T base) {
			return greaterThan(converter, base, null);
		}

		/**
		 * Indicates another input should be GREATER THAN to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static <T> Rule greaterThan(Converter<T> converter, T base, String message) {
			return new GreaterThanRule(message, converter, base);
		}

		/**
		 * Indicates another input should be LESS THAN to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 */
		public static Rule lessThan(Input input, Converter<?> converter) {
			return lessThan(input, converter, null);
		}

		/**
		 * Indicates another input should be LESS THAN to the given input
		 * 
		 * @param input
		 *            The base input to be used to compare other inputs
		 * @param converter
		 *            The converter to be used to convert both inputs
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static Rule lessThan(Input input, Converter<?> converter, String message) {
			return new LessThanRule(message, input, converter);
		}

		/**
		 * Indicates another input should be LESS THAN to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 */
		public static <T> Rule lessThan(Converter<T> converter, T base) {
			return lessThan(converter, base, null);
		}

		/**
		 * Indicates another input should be LESS THAN to the given base value
		 * 
		 * @param converter
		 *            The converter to be used to convert input
		 * 
		 * @param base
		 *            The base value to be used to validate input
		 * @param message
		 *            The message to be shown if validation fails
		 */
		public static <T> Rule lessThan(Converter<T> converter, T base, String message) {
			return new LessThanRule(message, converter, base);
		}
	}

}