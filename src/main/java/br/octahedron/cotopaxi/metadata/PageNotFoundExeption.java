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
package br.octahedron.cotopaxi.metadata;

import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;

/**
 * Indicates that the {@link MetatadaMapper} could not found a handler for an URL.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class PageNotFoundExeption extends Exception {

	private static final long serialVersionUID = -3510525396178721090L;
	private String url;
	private HTTPMethod httpMethod;

	protected PageNotFoundExeption(String url, HTTPMethod httpMethod) {
		super("Not found page for " + url + " - " + httpMethod.toString());
		this.url = url;
		this.httpMethod = httpMethod;
	}

	public String getUrl() {
		return this.url;
	}

	public HTTPMethod getHttpMethod() {
		return this.httpMethod;
	}
}