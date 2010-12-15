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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.octahedron.cotopaxi.model.attribute.validator.StringPatternValidator.CommonRegexs;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ValidatorTest {

	@Test
	public void rangeValidatorTest() {
		Validator<Integer> validator = new RangeValidator<Integer>(new Integer(2), new Integer(4));
		assertTrue(validator.isValid(new Integer(3)));
		assertFalse(validator.isValid(new Integer(2)));
		assertFalse(validator.isValid(new Integer(4)));
		assertFalse(validator.isValid(new Integer(10)));
		assertFalse(validator.isValid(new Integer(0)));

		validator = new RangeValidator<Integer>(new Integer(2), new Integer(4), true);
		assertTrue(validator.isValid(new Integer(3)));
		assertTrue(validator.isValid(new Integer(2)));
		assertTrue(validator.isValid(new Integer(4)));
		assertFalse(validator.isValid(new Integer(10)));
		assertFalse(validator.isValid(new Integer(0)));

		validator = new RangeValidator<Integer>(new Integer(2), null, true);
		assertTrue(validator.isValid(new Integer(3)));
		assertTrue(validator.isValid(new Integer(2)));
		assertTrue(validator.isValid(new Integer(100)));
		assertFalse(validator.isValid(new Integer(0)));

		validator = new RangeValidator<Integer>(null, new Integer(4), true);
		assertTrue(validator.isValid(new Integer(4)));
		assertTrue(validator.isValid(new Integer(2)));
		assertTrue(validator.isValid(new Integer(-10)));
		assertFalse(validator.isValid(new Integer(100)));

	}

	@Test
	public void stringPatternTest() {
		Validator<String> validator = new StringPatternValidator(CommonRegexs.EMAIL);
		assertTrue(validator.isValid("test@octahedron.com.br"));
		assertTrue(validator.isValid("test@octahedron.com"));
		assertTrue(validator.isValid("test@octahedron.br"));
		assertTrue(validator.isValid("test@octahedron.co.uk"));
		assertFalse(validator.isValid("test@.com"));
		assertFalse(validator.isValid("test@octahedron.com."));
		assertFalse(validator.isValid("test@com"));
		assertFalse(validator.isValid("@com"));

		validator = new StringPatternValidator(CommonRegexs.INTEGER);
		assertTrue(validator.isValid("912838"));
		assertTrue(validator.isValid("-912838"));
		assertTrue(validator.isValid("+912838"));
		assertFalse(validator.isValid("++912838"));
		assertFalse(validator.isValid("92.2"));
		assertFalse(validator.isValid("1i"));

		validator = new StringPatternValidator(CommonRegexs.FLOAT);
		assertTrue(validator.isValid("818181.0"));
		assertTrue(validator.isValid("818181,0"));
		assertTrue(validator.isValid("818181"));
		assertFalse(validator.isValid("92."));
		assertFalse(validator.isValid("1i"));

		validator = new StringPatternValidator(CommonRegexs.alfanumericAtLeastLength(5));
		assertTrue(validator.isValid("lalal"));
		assertTrue(validator.isValid("lalala"));
		assertFalse(validator.isValid("lala"));

		validator = new StringPatternValidator(CommonRegexs.alfanumericExactLength(5));
		assertTrue(validator.isValid("lalal"));
		assertFalse(validator.isValid("lala"));
		assertFalse(validator.isValid("lalala"));

		validator = new StringPatternValidator(CommonRegexs.alfanumericLength(3, 5));
		assertTrue(validator.isValid("lalal"));
		assertTrue(validator.isValid("lal"));
		assertTrue(validator.isValid("lala"));
		assertFalse(validator.isValid("la"));
		assertFalse(validator.isValid("lalala"));
	}

	@Test
	public void chainValidatorTest() {
		ChainValidator<String> validator = new ChainValidator<String>();
		validator.addValidator(new StringPatternValidator(CommonRegexs.EMAIL));
		validator.addValidator(new StringPatternValidator(CommonRegexs.nonBlankAtLeastLength(9)));

		assertTrue(validator.isValid("test@octahedron.com.br"));
		assertTrue(validator.isValid("me@none.co"));
		assertFalse(validator.isValid("a@b.com"));
	}
}
