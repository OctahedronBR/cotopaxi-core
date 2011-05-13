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

import br.octahedron.cotopaxi.controller.ParameterScope;
import br.octahedron.cotopaxi.inject.InstanceHandler;
import br.octahedron.cotopaxi.input.InputException;
import br.octahedron.cotopaxi.request.Request;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ParameterExtractorManager {

	/*
	 * extract parameter (request, beanClass)
	 */

	public <T> T extractParameter(ParameterScope scope, Request request, String parameterName, Class<T> parameterClass) throws InputException,
			InstantiationException {
		ParameterExtractor extractor = this.getExtractor(scope);
		return extractor.extractParameter(request, parameterName, parameterClass);
	}

	/**
	 * @param scope
	 * @return
	 * @throws InstantiationException
	 */
	private ParameterExtractor getExtractor(ParameterScope scope) throws InstantiationException {
		ParameterExtractor extractor = null;
		switch (scope) {
		case SESSION:
			extractor = InstanceHandler.getInstance(SessionParameterExtractor.class);
			break;
		case HEADER:
			extractor = InstanceHandler.getInstance(HeaderParameterExtractor.class);
			break;
		case COOKIE:
			extractor = InstanceHandler.getInstance(CookieParameterExtractor.class);
			break;
		default:
			extractor = InstanceHandler.getInstance(RequestParameterExtractor.class);
			break;
		}
		return extractor;
	}

}
