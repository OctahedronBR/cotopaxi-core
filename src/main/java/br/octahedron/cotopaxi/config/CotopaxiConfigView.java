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
package br.octahedron.cotopaxi.config;

import java.util.Collection;

import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.view.formatter.Formatter;

/**
 * A view for all Cotopaxi's configuration parameters.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public interface CotopaxiConfigView {

	public static final String JSON_FORMAT = "json";

	public static final String HTML_FORMAT = "html";

	/**
	 * The default folder name for internationalization files: "i18n";
	 */
	public static final String I18N_FOLDER = "i18n";

	/**
	 * The default page for handler errors: "{templates_root_folder}/error.vm"
	 */
	public static final String ERROR_HANDLER_TEMPLATE = "error.vm";

	/**
	 * The default page for handler not found error: "{templates_root_folder}/notfound.vm"
	 */
	public static final String NOT_FOUND_HANDLER_TEMPLATE = "notfound.vm";

	/**
	 * The default root folder for templates: "templates"
	 */

	public static final String TEMPLATES_ROOT_FOLDER = "templates";

	/**
	 * Gets the internationalization files' root folder
	 * 
	 * @see CotopaxiConfigView#I18N_FOLDER
	 * 
	 * @return the i18nFolder
	 */
	public abstract String getI18nFolder();

	/**
	 * Gets the not found error (HTTP result code 404) handler.
	 * 
	 * @see CotopaxiConfigView#NOT_FOUND_HANDLER_TEMPLATE
	 * 
	 * @return the notFoundPage
	 */
	public abstract String getNotFoundTemplate();

	/**
	 * Gets the server error (HTTP result code 500) handler.
	 * 
	 * @see CotopaxiConfigView#ERROR_HANDLER_TEMPLATE
	 * 
	 * @return the errorPage
	 */
	public abstract String getErrorTemplate();

	/**
	 * Gets templates files' root folder
	 * 
	 * @return the template files' root folder
	 */
	public abstract String getTemplateRoot();

	/**
	 * Indicates if has any global filter or not.
	 * 
	 * @return <code>true</code> if has at least one global filter, false otherwise.
	 */
	boolean hasGlobalFilters();

	/**
	 * Get the global filters, if exists.
	 * 
	 * @return the globalFilters list, or null if there's no global filter.
	 */
	public abstract Collection<Class<? extends Filter>> getGlobalFilters();

	/**
	 * Get the {@link CloudServicesFactory} to be used by this application
	 * 
	 * @return a {@link CloudServicesFactory} instance to be used.
	 */
	public abstract CloudServicesFactory getCloudServicesFactory();

	/**
	 * Check's if the given URL is a Redirect URL.
	 * 
	 * @param originalURL
	 *            The original URL that can be a Redirect
	 * @return <code>true</code> if is a redirect URL, <code>false</code> otherwise.
	 */
	public abstract boolean isRedirect(String originalURL);

	/**
	 * Get's the redirect destination's URL for the given URL.
	 * 
	 * @param originalURL
	 *            The original URL.
	 * @return The redirect destination URL, or null if the given URL isn't a redirect URL.
	 */
	public abstract String getRedirectDestUrl(String originalURL);

	/**
	 * Get's the formatter for the given format.
	 * 
	 * @see Formatter
	 * 
	 * @param format
	 *            The format.
	 * @return the Formatter for given format, or null if there's no Formatter registered for the
	 *         given format.
	 */
	public abstract Class<? extends Formatter> getFormatter(String format);

	/**
	 * Handles and provides access to the unique ControllerConfigView instance.
	 */
	public static class Handler {
		private static CotopaxiConfigView instance = new CotopaxiConfigViewImpl();

		/**
		 * Resets the unique instance of this object. To be used only on tests!
		 */
		public static void reset() {
			instance = new CotopaxiConfigViewImpl();
		}

		public static CotopaxiConfigView getConfigView() {
			return instance;
		}
	}

}