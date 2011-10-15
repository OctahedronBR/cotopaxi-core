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
package br.octahedron.cotopaxi.view.render;

import java.io.IOException;
import java.io.Writer;

import br.octahedron.util.Log;
import flexjson.JSONSerializer;

/**
 * JSONRender is responsible for rendering java objects into JSON format.
 * 
 * Generally used on controllers to render the attributes of request.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class JSONRender {

	private static final long serialVersionUID = -6755680559427788645L;
	private static final Log log = new Log(JSONRender.class);

	public void render(Object object, Writer writer) {
		try {
			writer.write(new JSONSerializer().prettyPrint(true).deepSerialize(object));
			log.debug("Written json in response writer");
		} catch (IOException ex) {
			log.terror("Impossible to parse objects into json format to be used on writer", ex);
			throw new RuntimeException(ex);
		}
	}
}
