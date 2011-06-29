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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

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
	

	public static void render(Object object, ServletRequest req, ServletResponse res) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
		// TODO refactor
		res.getWriter().write(new JSONSerializer().prettyPrint(true).serialize(object));
		log.debug("Written json in response writer");
		res.flushBuffer();
	}
}
