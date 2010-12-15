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

import java.util.regex.Pattern;

/**
 * A validator based on regex. It uses regular expression patterns to validate strings.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class StringPatternValidator implements Validator<String> {

	public enum CommonRegexs {

		ALFANUMERIC_CHAR("[a-zA-Z_0-9]"), NON_BLANK_CHAR("\\S"), BLANK_CHAR("\\s"), INTEGER("[+-]?[0-9]+"), FLOAT("[+-]?[0-9]+([.,][0-9])?"), EMAIL(
				"[A-Za-z0-9._+-]+@([A-Za-z0-9.-]+\\.)+[a-zA-Z]{2,3}");

		private String regex;

		CommonRegexs(String regex) {
			this.regex = regex;
		}

		String getRegex() {
			return this.regex;
		}

		public static String alfanumericExactLength(int minLength) {
			return ALFANUMERIC_CHAR.getRegex() + '{' + minLength + '}';
		}

		public static String alfanumericAtLeastLength(int minLength) {
			return ALFANUMERIC_CHAR.getRegex() + '{' + minLength + ",}+";
		}

		public static String alfanumericLength(int minLength, int maxLength) {
			return ALFANUMERIC_CHAR.getRegex() + '{' + minLength + ',' + maxLength + '}';
		}

		public static String nonBlankExactLength(int minLength) {
			return NON_BLANK_CHAR.getRegex() + '{' + minLength + '}';
		}

		public static String nonBlankAtLeastLength(int minLength) {
			return NON_BLANK_CHAR.getRegex() + '{' + minLength + ",}+";
		}

		public static String nonBlankLength(int minLength, int maxLength) {
			return NON_BLANK_CHAR.getRegex() + '{' + minLength + ',' + maxLength + '}';
		}
	}

	private Pattern pattern;

	public StringPatternValidator(Pattern pattern) {
		this.pattern = pattern;
	}

	public StringPatternValidator(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	public StringPatternValidator(CommonRegexs regex) {
		this.pattern = Pattern.compile(regex.getRegex());
	}

	@Override
	public boolean isValid(String object) {
		return this.pattern.matcher(object).matches();
	}
}
