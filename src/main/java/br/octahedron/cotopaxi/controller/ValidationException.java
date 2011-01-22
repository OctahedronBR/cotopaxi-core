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
package br.octahedron.cotopaxi.controller;

import java.util.Collection;

/**
 * Thrown to indicate and validation exception while getting model params.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = -1072466743781926291L;
	private Collection<String> invalidAttributes;

	public ValidationException(Collection<String> invalidAttributes) {
		this.invalidAttributes = invalidAttributes;
	}

	public Collection<String> getInvalidAttributes() {
		return this.invalidAttributes;
	}
}
