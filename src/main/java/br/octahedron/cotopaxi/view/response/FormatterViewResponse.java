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

import br.octahedron.cotopaxi.ResponseWrapper;
import br.octahedron.cotopaxi.view.formatter.Formatter;

/**
 * @author nome - email@octahedron.com.br
 * 
 */
public class FormatterViewResponse implements ViewResponse {

	private Formatter formatter;
	private ResultCode code;

	protected FormatterViewResponse(Formatter formatter, ResultCode code) {
		this.formatter = formatter;
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.view.ViewResponse#dispatch(br.octahedron.cotopaxi.ResponseWrapper)
	 */
	@Override
	public void dispatch(ResponseWrapper response) throws IOException {
		response.setResultCode(this.code);
		response.render(this.formatter);
	}

}
