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

import java.io.IOException;

import br.octahedron.cotopaxi.controller.ControllerContext;
import br.octahedron.util.Log;

/**
 * A simple {@link RenderableResponse} that renders a text content.
 * 
 * @author daniloqueiroz@octahedron.com.br
 */
public class SimpleTextResponse extends RenderableResponse {

	private static final String DEFAULT_CONTENT = "<html><head><head><body><h1>It works!</h1><br/></body></html>";
	private static final Log logger = new Log(SimpleTextResponse.class);

	private String content;

	public SimpleTextResponse(int code, ControllerContext context) {
		this(code, context, DEFAULT_CONTENT);
	}

	public SimpleTextResponse(int code, ControllerContext context, String content) {
		super(code, context.getOutput(), context.getCookies(), context.getHeaders(), context.getLocale());
		this.content = content;
	}

	@Override
	protected String getContentType() {
		return "text/html";
	}

	@Override
	protected void render() {
		try {
			this.writer.write(this.content);
		} catch (IOException ex) {
			logger.terror("Error writing response", ex);
		}
	}
}
