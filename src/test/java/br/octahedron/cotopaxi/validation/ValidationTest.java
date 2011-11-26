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

import static br.octahedron.cotopaxi.controller.Converter.Builder.bigIntNumber;
import static br.octahedron.cotopaxi.controller.Converter.Builder.intNumber;
import static br.octahedron.cotopaxi.controller.Converter.Builder.string;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

import br.octahedron.cotopaxi.validation.rule.EqualsRule;
import br.octahedron.cotopaxi.validation.rule.GreaterThanRule;
import br.octahedron.cotopaxi.validation.rule.LessThanRule;
import br.octahedron.cotopaxi.validation.rule.NotEqualsRule;
import br.octahedron.cotopaxi.validation.rule.RegexRule;
import br.octahedron.cotopaxi.validation.rule.RequiredRule;

/**
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ValidationTest {
	
	@Test
	public void regexRuleTest() {
		Rule regex = new RegexRule(null, "^(([0-9]{2}|\\([0-9]{2}\\))[ ])?[0-9]{4}[-. ]?[0-9]{4}$");
		assertTrue(regex.isValid("0000 0000"));
		assertTrue(regex.isValid("00 0000 0000"));
		assertTrue(regex.isValid("(00) 0000 0000"));
		assertTrue(regex.isValid("00000000"));
		assertTrue(regex.isValid("00 00000000"));
		
		regex = new RegexRule(null, "([a-zA-ZáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûÃÕãõçÇ] *){2,}");
		assertTrue(regex.isValid("Danilo Queiroz"));
		assertTrue(regex.isValid("Da"));
		regex = new RegexRule(null, ".*");
		assertTrue(regex.isValid(""));
		assertTrue(regex.isValid(null));
	}

	@Test
	public void requiredRuleTest() {
		Rule required = new RequiredRule(null);
		assertTrue(required.isValid("a"));
		assertFalse(required.isValid(""));
		assertFalse(required.isValid(null));
	}
	
	@Test
	public void equalsComparatorRuleTest() {
		Rule equals = new EqualsRule(string(), "test");
		assertTrue(equals.isValid("test"));
		assertFalse(equals.isValid("testt"));
		
		equals = new EqualsRule(new Input() {
			@Override
			public String getValue() {
				return "10";
			}
		}, bigIntNumber());
		assertTrue(equals.isValid("10"));
		assertFalse(equals.isValid("11"));
		assertFalse(equals.isValid("A"));
		
		equals = new EqualsRule(bigIntNumber(), new BigInteger("10"));
		assertTrue(equals.isValid("10"));
		assertFalse(equals.isValid("11"));
		assertFalse(equals.isValid("A"));
	}
	
	@Test
	public void notEqualsComparatorRuleTest() {
		Rule equals = new NotEqualsRule(string(), "test");
		assertFalse(equals.isValid("test"));
		assertTrue(equals.isValid("testt"));
		
		equals = new NotEqualsRule(new Input() {
			@Override
			public String getValue() {
				return "10";
			}
		}, bigIntNumber());
		assertFalse(equals.isValid("10"));
		assertTrue(equals.isValid("11"));
		assertFalse(equals.isValid("A"));
		
		equals = new NotEqualsRule(bigIntNumber(), new BigInteger("10"));
		assertFalse(equals.isValid("10"));
		assertTrue(equals.isValid("11"));
	}
	
	@Test
	public void lessThanRuleTest() {
		Rule rule = new LessThanRule(new Input() {
			@Override
			public String getValue() {
				return "10";
			}
		}, intNumber());
		assertTrue(rule.isValid("1"));
		assertFalse(rule.isValid("10"));
		assertFalse(rule.isValid("11"));
		assertFalse(rule.isValid("A"));
		
		rule = new LessThanRule(intNumber(), new Integer(10));
		assertTrue(rule.isValid("1"));
		assertFalse(rule.isValid("10"));
		assertFalse(rule.isValid("11"));
		assertFalse(rule.isValid("A"));
	}
	
	@Test
	public void greaterThanRuleTest() {
		Rule rule = new GreaterThanRule(new Input() {
			@Override
			public String getValue() {
				return "10";
			}
		}, intNumber());
		assertFalse(rule.isValid("1"));
		assertFalse(rule.isValid("10"));
		assertTrue(rule.isValid("11"));
		assertFalse(rule.isValid("A"));
		
		rule = new GreaterThanRule(intNumber(), new Integer(10));
		assertFalse(rule.isValid("1"));
		assertFalse(rule.isValid("10"));
		assertTrue(rule.isValid("11"));
		assertFalse(rule.isValid("A"));
	}
}
