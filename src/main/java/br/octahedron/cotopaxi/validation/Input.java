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

import br.octahedron.cotopaxi.controller.InputController;

/**
 * @author Name - email@octahedron.com.br
 *
 */
interface Input {
	
	/**
	 * Returns the Field's value
	 * @return the Field's value
	 */
	public String getValue();
	
	static class LiteralInput implements Input {
		private String value;

		LiteralInput(String value) {
			this.value = value;
		}

		@Override
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	static class AttributeInput extends InputController implements Input {
		protected String attributeName;

		AttributeInput(String attributeName) {
			this.attributeName = attributeName;
		}

		@Override
		public String getValue() {
			return this.in(this.attributeName);
		}
		
		@Override
		public String toString() {
			return this.attributeName;
		}
	}
	
	static class SessionInput extends AttributeInput {
		SessionInput(String attributeName) {
			super(attributeName);
		}

		@Override
		public String getValue() {
			return (String) this.session(this.attributeName);
		}
	}
	
	static class HeaderInput extends AttributeInput {
		HeaderInput(String headerName) {
			super(headerName);
		}

		@Override
		public String getValue() {
			return (String) this.header(this.attributeName);
		} 
	}
	
	static class CookieInput extends AttributeInput {
		CookieInput(String cookieName) {
			super(cookieName);
		}

		@Override
		public String getValue() {
			return (String) this.cookie(this.attributeName);
		} 
	}
}
