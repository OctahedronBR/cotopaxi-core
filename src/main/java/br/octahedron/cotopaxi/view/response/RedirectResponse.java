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

import javax.servlet.http.HttpServletResponse;

import br.octahedron.util.Log;

/**
 * A redirect Response
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RedirectResponse extends ServletGenericResponse {

	private static final Log log = new Log(RedirectResponse.class);
	private String url;

	public RedirectResponse(String url) {
		this.url = url;
	}

	/**
	 * Gets the url to redirect.
	 * 
	 * @return the url
	 */
	public String redirectUrl() {
		return url;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public void dispatch(HttpServletResponse servletResponse) throws IOException {
		log.debug("Sending redirect to %s", this.url);
		servletResponse.sendRedirect(this.url);
	}
}