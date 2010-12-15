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

import java.util.Locale;
import java.util.Map;

import flexjson.JSON;
import flexjson.JSONSerializer;
import br.octahedron.cotopaxi.view.ContentType;

/**
 * A simple json formatter using FlexJSON. It serializes the attribute maps and can use the
 * {@link JSON} annotation to specify attributes to be serialized.
 * 
 * http://flexjson.sourceforge.net/
 * 
 * @see JSONSerializer
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class SimpleJSONFormatter extends Formatter {

	public SimpleJSONFormatter() {
		super(ContentType.JSON);
	}

	public SimpleJSONFormatter(Map<String, Object> attributes, Locale lc) {
		this();
		this.setAttributes(attributes);
		this.setLocale(lc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.view.formatter.Formatter#doFormat()
	 */
	@Override
	protected String doFormat() {
		return new JSONSerializer().prettyPrint(true).serialize(this.attributes);
	}
}
