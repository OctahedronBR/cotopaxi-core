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
import br.octahedron.cotopaxi.controller.BaseController;
import br.octahedron.cotopaxi.validation.rule.ValidationRule;
import br.octahedron.util.Log;

/**
 * This entity is responsible by Validates inputs using validation rules;
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public final class Validator extends BaseController {

	private static final Log log = new Log(Validator.class);
	private List<RuleEntry> rules = new LinkedList<RuleEntry>();

	/**
	 * Adds an validation rule to given attribute
	 * 
	 * @param rule
	 *            The rule to be added
	 * @param message
	 *            The message to be shown if attribute invalid
	 * @param attribute
	 *            The attribute to be validated using the given rule
	 */
	public void add(ValidationRule rule, String message, String attribute) {
		RuleEntry entry = new RuleEntry(rule, message, attribute);
		this.rules.add(entry);
	}

	/**
	 * Adds an validation rule to given attributes.
	 * 
	 * It's the same as call {@link Validator#add(ValidationRule, String, String)} multiples times.
	 * 
	 * 
	 * @param rule
	 *            The rule to be added
	 * @param message
	 *            The message to be shown if attribute invalid
	 * @param attribute
	 *            The attributes to be validated using the given rule
	 */
	public void add(ValidationRule rule, String message, String... attributes) {
		for (String attribute : attributes) {
			this.add(rule, message, attribute);
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
		for (RuleEntry entry : this.rules) {
			String att = entry.getAttribute();
			boolean validRule = entry.getRule().isValid(this.in(att));
			if (!validRule) {
				valid = false;
				if (!invalidMessages.containsKey(att)) {
					invalidMessages.put(att, entry.getMessage());
				}
			}
		}
		log.debug("Validation result: %b. Invalid attributes: count %d; list %s", valid, invalidMessages.size(), invalidMessages.keySet().toString());
		this.out(getProperty(CotopaxiProperty.INVALID_PROPERTY), invalidMessages);
		return valid;
	}

	
	
	/**
	 * Stores all data related to a rule validation registry
	 */
	private class RuleEntry {
		private String attribute;
		private String message;
		private ValidationRule rule;

		public RuleEntry(ValidationRule rule, String message, String attribute) {
			this.rule = rule;
			this.message = message;
			this.attribute = attribute;
		}

		/**
		 * @return the attribute
		 */
		public String getAttribute() {
			return attribute;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @return the rule
		 */
		public ValidationRule getRule() {
			return rule;
		}
	}
}
