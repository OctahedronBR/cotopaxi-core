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
package br.octahedron.cotopaxi.input.extract;

import javax.servlet.http.Cookie;

import br.octahedron.cotopaxi.input.InputException;
import br.octahedron.cotopaxi.input.convert.ConverterManager;
import br.octahedron.cotopaxi.request.Request;

/**
 * Retrieves a parameter's from {@link Cookie}s.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 *
 */
public class CookieParameterExtractor implements ParameterExtractor {
	
	private ConverterManager converter = ConverterManager.getInstance();

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.input.extract.ParameterExtractor#extractParameter(br.octahedron.cotopaxi.request.Request, java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T extractParameter(Request request, String parameterName, Class<T> parameterClass) throws InputException {
		for(Cookie cookie : request.getCookies()) {
			if (cookie.getName().equals(parameterName)) {
				return converter.convert(parameterClass, cookie.getValue());
			}
		}
		return null;
	}
}
