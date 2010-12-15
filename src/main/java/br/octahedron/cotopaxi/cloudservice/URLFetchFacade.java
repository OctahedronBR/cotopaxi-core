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
package br.octahedron.cotopaxi.cloudservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import br.octahedron.cotopaxi.cloudservice.common.HTTPMethod;


/**
 * Facade for URL fetch service.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public interface URLFetchFacade {

	/**
	 * Retrieves a specified URL and return the response of request as {@link InputStream}. Assumes
	 * that is a GET request.
	 * 
	 * @param url
	 *            The url to fetch.
	 * @return The content result of fetch.
	 */
	public InputStream fetchAsInputStream(String url) throws IOException;

	/**
	 * Retrieves a specified URL and return the response of request as {@link InputStream}.
	 * 
	 * @param url
	 *            The url to fetch.
	 * @param method
	 *            The method type of fetch.
	 * @param params
	 *            The parameters of request.
	 * @return The content result of fetch.
	 */
	public InputStream fetchAsInputStream(String url, HTTPMethod method, Map<String, String> params) throws IOException;

	/**
	 * Retrieves a specified URL and return the response of request as {@link Document}. Assumes
	 * that is a GET request. Remember that Document class just parse XML content. So, if you need
	 * another type of response, try use {@link #fetchAsInputStream(String)}.
	 * 
	 * @param url
	 *            The url to fetch.
	 * @param method
	 *            The method type of fetch.
	 * @return The content result of fetch.
	 */
	public Document fetchAsDocument(String url) throws SAXException, IOException;

	/**
	 * Retrieves a specified URL and return the response of request as {@link Document}. Remember
	 * that Document class just parse XML content. So, if you need another type of response, try use
	 * {@link #fetchAsInputStream(String, HTTPMethod, Map)}.
	 * 
	 * @param url
	 *            The url to fetch.
	 * @param method
	 *            The method type of fetch.
	 * @param params
	 *            The parameters of request.
	 * @return The content result of fetch.
	 */
	public Document fetchAsDocument(String url, HTTPMethod method, Map<String, String> params) throws SAXException, IOException;

	/**
	 * Checks if a specific url content has changed.
	 * 
	 * @param url
	 *            The url to check its content.
	 * @return <code>true</code> if content has changed, <code>false</code> otherwise.
	 */
	public boolean hasChanged(String url) throws IOException;

}
