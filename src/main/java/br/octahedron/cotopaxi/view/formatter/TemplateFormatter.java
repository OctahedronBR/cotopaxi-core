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
package br.octahedron.cotopaxi.view.formatter;

import br.octahedron.cotopaxi.view.ContentType;

/**
 * An AbstractFormatter that uses a template to format the content.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class TemplateFormatter extends Formatter {

	protected String template;

	public TemplateFormatter(ContentType contentType) {
		super(contentType);
	}

	@Override
	public boolean isReady() {
		return super.isReady() && this.template != null;
	}

	/**
	 * Sets this formatter template
	 * 
	 * @param templateFile
	 *            the template to be used by this formatter
	 */
	public void setTemplate(String templateFile) {
		this.template = templateFile;
	}

}
