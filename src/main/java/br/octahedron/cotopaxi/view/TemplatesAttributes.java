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
package br.octahedron.cotopaxi.view;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public enum TemplatesAttributes {

	URL_NOT_FOUND_ATTRIBUTE("pageNotFound"), URL_NOT_FOUND_METHOD_ATTRIBUTE("pageNotFoundMethod"), INVALIDATION_FIELDS_ATTRIBUTE("invalidFields"), EXCEPTION_ATTRIBUTE(
			"exception"), EXCEPTION_CLASS_ATTRIBUTE("exceptionClass"), EXCEPTION_MESSAGE_ATTRIBUTE("exceptionMessage"), EXCEPTION_STACK_TRACE_ATTRIBUTE(
			"exceptionStackTrace"), MESSAGE_ON_SUCCESS("successMessage"), MESSAGE_ON_ERROR("errorMessage"), MESSAGE_ON_VALIDATION_FAILS(
			"validationMessage");

	private String attributeKey;

	private TemplatesAttributes(String attributeKey) {
		this.attributeKey = attributeKey;
	}

	public String getAttributeKey() {
		return this.attributeKey;
	}
}
