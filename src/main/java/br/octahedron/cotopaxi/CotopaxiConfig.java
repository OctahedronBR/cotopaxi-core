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
package br.octahedron.cotopaxi;

import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.controller.auth.UserLookupStrategy;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.view.formatter.Formatter;

/**
 * This entity provides methods to change Cotopaxi's configuration.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public interface CotopaxiConfig {

	/**
	 * Sets the internationalization files' folder.
	 * 
	 * @param i18nFolder
	 *            the i18nFolder to set
	 */
	public abstract void setI18nFolder(String i18nFolder);

	/**
	 * Sets the server forbidden handler.
	 */
	public abstract void setForbiddenTemplate(String forbiddenHandler);

	/**
	 * Sets the server error handler.
	 */
	public abstract void setErrorTemplate(String errorHandler);

	/**
	 * Sets the not found error handler.
	 */
	public abstract void setNotFoundTemplate(String notFoundHandler);

	/**
	 * Sets the templates' root folder.
	 * 
	 * @param templatesRoot
	 *            the templates root folder
	 */
	public abstract void setTemplatesRoot(String templatesRoot);

	/**
	 * Sets the {@link UserLookupStrategy} to be used by the authentication mechanism
	 * 
	 * @see UserLookupStrategy
	 * 
	 * @param strategy
	 *            the {@link UserLookupStrategy}
	 */
	public abstract void setUserLookupStrategy(UserLookupStrategy strategy);

	/**
	 * Sets the {@link CloudServicesFactory} to be used by this application.
	 * 
	 * @param factory
	 *            The factory to be used.
	 */
	public abstract void setCloudServicesFactory(CloudServicesFactory factory);

	/**
	 * Adds a Global Filter.
	 * 
	 * @param filter
	 *            the filter.
	 */
	public abstract void addGlobalFilter(Class<? extends Filter> filter);

	/**
	 * Set's a formatter for a given format.
	 * 
	 * @see Formatter
	 * 
	 * @param format
	 *            The format.
	 * @param formatterClass
	 *            The formatter class for the given format.
	 */
	public abstract void setFormatter(String format, Class<? extends Formatter> formatterClass);

	/**
	 * Sets a redirection from originalURL to destURL. All requests for originalURL will be redirect
	 * to destURL.
	 * 
	 * @param originalURL
	 *            the URL to be redirect.
	 * @param destURL
	 *            the URL which requests will be redirected to.
	 */
	public abstract void setRedirect(String originalURL, String destURL);

	/**
	 * Adds a model facade to Cotopaxi. ModelFacade, such as the name says, represents a facade
	 * between the controller and the model, and should provide the necessary metadata to maps
	 * request to the model.
	 * 
	 * The modelFacade class should has an empty constructor.
	 * 
	 * @param modelFacadeClass
	 *            the model facade class. the class
	 */
	public abstract void addModelFacade(Class<?>... modelFacadeClass);

}