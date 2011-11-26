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

import static br.octahedron.cotopaxi.CotopaxiProperty.getProperty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.octahedron.cotopaxi.CotopaxiProperty;
import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.validation.rule.AbstractRule;
import br.octahedron.util.Log;

/**
 * This entity is responsible by Validates inputs using validation rules;
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public final class Validator {

	private static final Log log = new Log(Validator.class);
	private List<RuleEntry> entries = new LinkedList<RuleEntry>();
	private Output out = new Output();

	/**
	 * Adds a new attribute to be validated using the given rule.
	 * 
	 * @param attribute
	 *            The attribute to be validated
	 * @param rule
	 *            The {@link AbstractRule} to be used to validate the attribute
	 */
	public void add(String attribute, Rule rule) {
		this.entries.add(new RuleEntry(rule, new Input.AttributeInput(attribute)));
	}

	/**
	 * Adds a new attribute to be validated using the given rule.
	 * 
	 * @param input
	 *            The input to be validated
	 * @param rule
	 *            The {@link AbstractRule} to be used to validate the attribute
	 */
	public void add(Input input, Rule rule) {
		this.entries.add(new RuleEntry(rule, input));
	}

	/**
	 * Adds a new attribute to be validated using the given rules.
	 * 
	 * @param attribute
	 *            The attribute to be validated
	 * @param rules
	 *            The {@link AbstractRule} to be used to validate the attribute
	 */
	public void add(String attribute, Rule... rules) {
		for (Rule rule : rules) {
			this.add(attribute, rule);
		}
	}

	/**
	 * Adds a new attribute to be validated using the given rules.
	 * 
	 * @param input
	 *            The input to be validated
	 * @param rules
	 *            The {@link AbstractRule} to be used to validate the attribute
	 */
	public void add(Input input, Rule... rules) {
		for (Rule rule : rules) {
			this.add(input, rule);
		}
	}

	/**
	 * Check if this validator is valid. If no input parameter was set previously, it should return
	 * true.
	 * 
	 * @return <code>true</code> if all input parameters are valid, <code>false</code> otherwise.
	 */
	public boolean isValid() {
		Map<String, String> invalidMessages = new HashMap<String, String>();
		boolean valid = true;
		for (RuleEntry entry : this.entries) {
			Rule rule = entry.getRule();
			Input input = entry.getIput();
			String key = input.toString();

			if (!invalidMessages.containsKey(key)) {
				log.debug("Validating '%s' using rule %s", key, rule);
				boolean validRule = rule.isValid(input.getValue());
				if (!validRule) {
					log.debug("Input '%s' invalid by %s", key, rule);
					valid = false;
					invalidMessages.put(key, rule.getMessage());
				}
			}
		}
		log.debug("Invalid attributes: count %d - list %s", invalidMessages.size(), invalidMessages.keySet().toString());
		this.out.add(getProperty(CotopaxiProperty.INVALID_PROPERTY), invalidMessages);
		return valid;
	}

	// Useful internal classes

	/**
	 * Encapsulate all rule data
	 */
	private class RuleEntry {
		private Input attribute;
		private Rule rule;

		public RuleEntry(Rule rule, Input attribute) {
			this.rule = rule;
			this.attribute = attribute;
		}

		/**
		 * @return the attribute
		 */
		public Input getIput() {
			return attribute;
		}

		/**
		 * @return the rule
		 */
		public Rule getRule() {
			return rule;
		}
	}

	/**
	 * Provides access to {@link Controller#out} method.
	 * 
	 * TODO review this strategy
	 */
	private class Output extends Controller {
		public void add(String key, Object value) {
			this.out(key, value);
		}
	}
}
