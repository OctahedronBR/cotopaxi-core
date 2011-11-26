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
import br.octahedron.cotopaxi.validation.Rule;

/**
 * This class is the base class for validation {@link Rule} that need to compare/validate the input
 * value with an base value.
 * 
 * This base value can be another input value (an input/session attribute, a session attribute, a
 * cookie/header value) or a given object. However, you should assume that at this point the base
 * object is a valid reference for this validation.
 * 
 * It permits to create complex validation chains. For example, lets suppose that we want to
 * validate a password form, that submit three password fields: the current password, the new
 * password, and a verification field to the new password. In this case, we should do the following
 * verifications:
 * 
 * <pre>
 * 		1 - the current password isn't null/empty; 
 * 		
 * 		2 - the new password isn't null/empty, is the minimum required length and are not equals to
 * 		current password; 
 * 
 * 		3 - the password verification field is equals to the new password field.
 * </pre>
 * 
 * We must do all these three validations to avoid do wrong assumptions, such only check if the new
 * password and new password verification are equals (they can be both empty, for example).
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class ComparableRule extends AbstractRule {

	private Input input;
	private Converter<?> converter;
	private Object original;

	/**
	 * @param baseInput
	 *            The base {@link Input} that will be used as reference to compare another input
	 *            value
	 * @param converter
	 *            The {@link Converter} to be used to convert both the baseInput as the input being
	 *            validate
	 */
	public ComparableRule(Input baseInput, Converter<?> converter) {
		this(null, baseInput, converter);
	}

	/**
	 * @param message
	 *            The message for validation fail
	 * @param baseInput
	 *            The base {@link Input} that will be used as reference to compare another input
	 *            value
	 * @param converter
	 *            The {@link Converter} to be used to convert both the baseInput as the input being
	 *            validate
	 */
	public ComparableRule(String message, Input baseInput, Converter<?> converter) {
		super(message);
		this.input = baseInput;
		this.converter = converter;
	}

	/**
	 * @param converter
	 *            The {@link Converter} to be used to convert the input being validate
	 * @param baseLiteral
	 *            The base literal value to be used to compare with input. This literal doesn't need
	 *            to be converted
	 */
	public <T> ComparableRule(Converter<T> converter, T baseLiteral) {
		this(null, converter, baseLiteral);
	}

	/**
	 * @param message
	 *            The message for validation fail
	 * @param converter
	 *            The {@link Converter} to be used to convert the input being validate
	 * @param baseLiteral
	 *            The base literal value to be used to compare with input. This literal doesn't need
	 *            to be converted
	 */
	public <T> ComparableRule(String message, Converter<T> converter, T baseLiteral) {
		super(message);
		this.converter = converter;
		this.original = baseLiteral;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public final boolean isValid(String input) {
		Object orig = (this.input != null) ? this.converter.convert(this.input.getValue()) : this.original;
		Object other = converter.convert(input);
		if (orig != null && other != null) {
			return this.compare(orig, other);
		} else {
			return false;
		}
	}

	/**
	 * @param base
	 *            The base object which the other object will be compared with.
	 * @param other
	 *            The input object being compared
	 * @return <code>true</code> if the other object is valid, comparing to the base one,
	 *         <code>false</code> otherwise.
	 */
	protected abstract <T> boolean compare(T base, T other);

}
