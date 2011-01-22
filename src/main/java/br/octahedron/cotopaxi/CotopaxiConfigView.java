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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.view.formatter.Formatter;

/**
 * A view for all Cotopaxi's configuration parameters.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class CotopaxiConfigView {

	/**
	 * RSS format extension
	 */
	public static final String RSS_FORMAT = "rss";

	/**
	 * ATOM format extension
	 */
	public static final String ATOM_FORMAT = "atom";

	/**
	 * JSON format extension
	 */
	public static final String JSON_FORMAT = "json";

	/**
	 * HTML format extension
	 */
	public static final String HTML_FORMAT = "html";

	/**
	 * The default folder name for internationalization files: "i18n";
	 */
	public static final String I18N_FOLDER = "i18n";

	/**
	 * The default page for forbidden errors: "{templates_root_folder}/forbidden.vm"
	 */
	public static final String FORBIDDEN_HANDLER_TEMPLATE = "forbidden.vm";

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

	protected String i18nFolder = I18N_FOLDER;
	protected String forbiddenTemplate = FORBIDDEN_HANDLER_TEMPLATE;
	protected String errorTemplate = ERROR_HANDLER_TEMPLATE;
	protected String notFoundTemplate = NOT_FOUND_HANDLER_TEMPLATE;
	protected String templatesRoot = TEMPLATES_ROOT_FOLDER;
	protected Map<String, Class<? extends Formatter>> formatters = new HashMap<String, Class<? extends Formatter>>();
	protected Collection<Class<? extends Filter>> globalFilters;
	protected Map<String, String> redirects;

	private CotopaxiConfig controllerConfig;

	public LinkedList<Class<?>> modelFacades;

	public CotopaxiConfigView() {
		this.controllerConfig = new CotopaxiConfigImpl();
	}

	protected CotopaxiConfig getCotopaxiConfig() {
		return this.controllerConfig;
	}

	/**
	 * Gets the forbidden access (HTTP result code 403) handler.
	 * 
	 * @see CotopaxiConfigView#FORBIDDEN_HANDLER_TEMPLATE
	 * 
	 * @return the forbiddentPage
	 */
	public String getForbiddenTemplate() {
		return this.forbiddenTemplate;
	}

	/**
	 * Gets the server error (HTTP result code 500) handler.
	 * 
	 * @see CotopaxiConfigView#ERROR_HANDLER_TEMPLATE
	 * 
	 * @return the errorPage
	 */
	public String getErrorTemplate() {
		return this.errorTemplate;
	}

	/**
	 * Gets the not found error (HTTP result code 404) handler.
	 * 
	 * @see CotopaxiConfigView#NOT_FOUND_HANDLER_TEMPLATE
	 * 
	 * @return the notFound page template
	 */
	public String getNotFoundTemplate() {
		return this.notFoundTemplate;
	}

	/**
	 * Gets templates files' root folder
	 * 
	 * @return the template files' root folder
	 */
	public String getTemplateRoot() {
		return this.templatesRoot;
	}

	/**
	 * Gets the internationalization files' root folder
	 * 
	 * @see CotopaxiConfigView#I18N_FOLDER
	 * 
	 * @return the i18nFolder
	 */
	public String getI18nFolder() {
		return this.i18nFolder;
	}

	/**
	 * Indicates if has any global filter or not.
	 * 
	 * @return <code>true</code> if has at least one global filter, false otherwise.
	 */

	public boolean hasGlobalFilters() {
		return this.globalFilters != null;
	}

	/**
	 * Get the global filters, if exists.
	 * 
	 * @return the globalFilters list, or null if there's no global filter.
	 */
	public Collection<Class<? extends Filter>> getGlobalFilters() {
		return this.globalFilters;
	}

	/**
	 * Check's if the given URL is a Redirect URL.
	 * 
	 * @param originalURL
	 *            The original URL that can be a Redirect
	 * @return <code>true</code> if is a redirect URL, <code>false</code> otherwise.
	 */
	public boolean isRedirect(String originalURL) {
		if (this.redirects != null) {
			return this.redirects.containsKey(originalURL);
		} else {
			return false;
		}
	}

	/**
	 * Get's the redirect destination's URL for the given URL.
	 * 
	 * @param originalURL
	 *            The original URL.
	 * @return The redirect destination URL, or null if the given URL isn't a redirect URL.
	 */

	public String getRedirectDestUrl(String originalURL) {
		if (this.redirects != null) {
			return this.redirects.get(originalURL);
		} else {
			return null;
		}
	}

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
	public Class<? extends Formatter> getFormatter(String format) {
		return this.formatters.get(format);
	}

	/**
	 * Gets all the models facades registered to Cotopaxi
	 * 
	 * @return A {@link Collection} with all added model facades.
	 * @see CotopaxiConfig#addModelFacade(Class)
	 */
	public Collection<Class<?>> getModelFacades() {
		return this.modelFacades;
	}

	private class CotopaxiConfigImpl implements CotopaxiConfig {

		@Override
		public void setI18nFolder(String i18nFld) {
			CotopaxiConfigView.this.i18nFolder = i18nFld;
		}

		@Override
		public void setErrorTemplate(String errorTPL) {
			CotopaxiConfigView.this.errorTemplate = errorTPL;
		}

		@Override
		public void setNotFoundTemplate(String notFoundTPL) {
			CotopaxiConfigView.this.notFoundTemplate = notFoundTPL;
		}

		@Override
		public void setTemplatesRoot(String tplRoot) {
			CotopaxiConfigView.this.templatesRoot = tplRoot;
		}

		@Override
		public void addGlobalFilter(Class<? extends Filter> filter) {
			if (CotopaxiConfigView.this.globalFilters == null) {
				CotopaxiConfigView.this.globalFilters = new LinkedList<Class<? extends Filter>>();
			}
			CotopaxiConfigView.this.globalFilters.add(filter);
		}

		@Override
		public void setRedirect(String originalURL, String destURL) {
			if (CotopaxiConfigView.this.redirects == null) {
				CotopaxiConfigView.this.redirects = new HashMap<String, String>();
			}
			CotopaxiConfigView.this.redirects.put(originalURL, destURL);
		}

		@Override
		public void setFormatter(String format, Class<? extends Formatter> formatterClass) {
			CotopaxiConfigView.this.formatters.put(format, formatterClass);
		}

		@Override
		public void addModelFacade(Class<?>... modelFacadeClass) {
			if (CotopaxiConfigView.this.modelFacades == null) {
				CotopaxiConfigView.this.modelFacades = new LinkedList<Class<?>>();
			}
			CotopaxiConfigView.this.modelFacades.addAll(Arrays.asList(modelFacadeClass));
		}

		@Override
		public void setForbiddenTemplate(String forbiddenHandler) {
			CotopaxiConfigView.this.forbiddenTemplate = forbiddenHandler;
		}
	}
}