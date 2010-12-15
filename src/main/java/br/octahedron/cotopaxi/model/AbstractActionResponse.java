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
package br.octahedron.cotopaxi.model;

import java.util.HashMap;
import java.util.Map;

import br.octahedron.cotopaxi.view.TemplatesAttributes;

/**
 * An abstract {@link ActionResponse}.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public abstract class AbstractActionResponse implements ActionResponse {

	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Result result;

	public AbstractActionResponse(Result result) {
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.model.ActionResponse#getResult()
	 */
	public Result getResult() {
		return this.result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.model.ActionResponse#getAttributes()
	 */
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebr.octahedron.cotopaxi.model.ActionResponse#setAttribute(br.octahedron.cotopaxi.view.
	 * TemplatesAttributes, java.lang.Object)
	 */
	public void setAttribute(TemplatesAttributes key, Object value) {
		this.attributes.put(key.getAttributeKey(), value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.model.ActionResponse#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}

}