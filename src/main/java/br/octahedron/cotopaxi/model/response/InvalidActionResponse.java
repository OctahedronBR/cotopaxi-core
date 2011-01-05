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
package br.octahedron.cotopaxi.model.response;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a VALIDATION_FAILED {@link ActionResponse}.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class InvalidActionResponse extends AbstractActionResponse {

	private Collection<String> invalidAttributes;

	public InvalidActionResponse(String... invalidAttributes) {
		super(Result.VALIDATION_FAILED);
		this.invalidAttributes = Arrays.asList(invalidAttributes);
	}

	public InvalidActionResponse(Collection<String> invalidAttributes) {
		super(Result.VALIDATION_FAILED);
		this.invalidAttributes = invalidAttributes;
	}

	public Collection<String> getInvalidAttributes() {
		return this.invalidAttributes;
	}
}
