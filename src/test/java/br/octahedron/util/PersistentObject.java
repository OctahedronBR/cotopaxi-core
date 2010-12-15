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
package br.octahedron.util;

import javax.jdo.annotations.PrimaryKey;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class PersistentObject {

	@PrimaryKey
	private String key;

	public PersistentObject(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PersistentObject) ? this.key.equals(((PersistentObject) obj).getKey()) : false;
	}
}