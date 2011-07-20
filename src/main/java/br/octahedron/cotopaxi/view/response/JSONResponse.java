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
package br.octahedron.cotopaxi.view.response;

import br.octahedron.cotopaxi.controller.ControllerContext;
import br.octahedron.cotopaxi.view.render.JSONRender;

/**
 * A {@link InterceptableResponse} that render and write objects in JSON format.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 */
public class JSONResponse extends InterceptableResponse {

	private static final JSONRender jsonRender = new JSONRender();

	/**
	 * 
	 */
	public JSONResponse(int code, ControllerContext context) {
		super(JSONResponse.class, code, context.getOutput(), context.getCookies(), context.getHeaders(), context.getLocale());
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.view.response.WriteableResponse#getContentType()
	 */
	@Override
	public String getContentType() {
		return "application/json";
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.view.response.InterceptableResponse#render()
	 */
	@Override
	protected void render() {
		jsonRender.render(this.output, this.writer);
	}

}
