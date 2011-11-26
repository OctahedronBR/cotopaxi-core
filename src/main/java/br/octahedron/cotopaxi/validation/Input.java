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
 * Specify input sources to be validated.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface Input {

	/**
	 * Returns the Field's value
	 * 
	 * @return the Field's value
	 */
	public String getValue();

	/**
	 * A builder for basic validation rules and input
	 */
	public static class Builder {

		/**
		 * An Attribute {@link Input}
		 * 
		 * @param attributeName
		 *            The attribute's name
		 */
		public static Input attribute(String attributeName) {
			return new AttributeInput(attributeName);
		}

		/**
		 * A session {@link Input}
		 * 
		 * @param attributeName
		 *            The session's attribute name
		 */
		public static Input session(String attributeName) {
			return new SessionInput(attributeName);
		}

		/**
		 * A header {@link Input}
		 * 
		 * @param headerName
		 *            The header's name
		 */
		public static Input header(String headerName) {
			return new HeaderInput(headerName);
		}

		/**
		 * A cookie {@link Input}
		 * 
		 * @param cookieName
		 *            The cookie's name
		 */
		public static Input cookie(String cookieName) {
			return new CookieInput(cookieName);
		}
	}

	/**
	 * Attribute {@link Input} implementation
	 */
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

	/**
	 * Session {@link Input} implementation
	 */
	static class SessionInput extends AttributeInput {
		SessionInput(String attributeName) {
			super(attributeName);
		}

		@Override
		public String getValue() {
			return (String) this.session(this.attributeName);
		}
	}

	/**
	 * Header {@link Input} implementation
	 */
	static class HeaderInput extends AttributeInput {
		HeaderInput(String headerName) {
			super(headerName);
		}

		@Override
		public String getValue() {
			return (String) this.header(this.attributeName);
		}
	}

	/**
	 * Cookie {@link Input} implementation
	 */
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
