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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import br.octahedron.cotopaxi.CotopaxiServlet;
import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.view.formatter.Formatter;

/**
 * The controller configuration. It makes possible do set/modify many Controller configuration
 * parameters.
 * 
 * Once this class is a modifiable version of the ControllerConfigView, it's only seen by the
 * {@link CotopaxiServlet} and the {@link CotopaxiConfigurator}. Entities that need read access to
 * the Controller Configurations parameters will access them using a {@link CotopaxiConfigView}.
 * 
 * For understand the configuration workflow, see the internal comments at the
 * {@link CotopaxiServlet} class.
 * 
 * @see CotopaxiConfigView
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class CotopaxiConfigViewImpl implements CotopaxiConfigView {

	private String i18nFolder = I18N_FOLDER;
	private String errorHandler = ERROR_HANDLER_TEMPLATE;
	private String notFoundHandler = NOT_FOUND_HANDLER_TEMPLATE;
	private String templatesRoot = TEMPLATES_ROOT_FOLDER;
	private Map<String, Class<? extends Formatter>> formatters = new HashMap<String, Class<? extends Formatter>>();
	private Collection<Class<? extends Filter>> globalFilters;
	private Map<String, String> redirects;
	private CloudServicesFactory factory;

	private CotopaxiConfigImpl controllerConfig;

	protected CotopaxiConfigViewImpl() {
		this.controllerConfig = new CotopaxiConfigImpl();
	}

	public CotopaxiConfig getControllerConfig() {
		return this.controllerConfig;
	}

	@Override
	public String getErrorTemplate() {
		return this.errorHandler;
	}

	@Override
	public String getNotFoundTemplate() {
		return this.notFoundHandler;
	}

	@Override
	public String getTemplateRoot() {
		return this.templatesRoot;
	}

	@Override
	public String getI18nFolder() {
		return this.i18nFolder;
	}

	@Override
	public boolean hasGlobalFilters() {
		return this.globalFilters != null;
	}

	@Override
	public Collection<Class<? extends Filter>> getGlobalFilters() {
		return this.globalFilters;
	}

	@Override
	public CloudServicesFactory getCloudServicesFactory() {
		return this.factory;
	}

	@Override
	public boolean isRedirect(String originalURL) {
		if (this.redirects != null) {
			return this.redirects.containsKey(originalURL);
		} else {
			return false;
		}
	}

	@Override
	public String getRedirectDestUrl(String originalURL) {
		if (this.redirects != null) {
			return this.redirects.get(originalURL);
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends Formatter> getFormatter(String format) {
		return this.formatters.get(format);
	}

	private class CotopaxiConfigImpl implements CotopaxiConfig {

		/*
		 * (non-Javadoc)
		 * 
		 * @see br.octahedron.controller.ControllerConfig#setI18nFolder(java.lang.String)
		 */
		public void setI18nFolder(String i18nFolder) {
			CotopaxiConfigViewImpl.this.i18nFolder = i18nFolder;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see br.octahedron.controller.ControllerConfig#setErrorHandler(java.lang.String)
		 */
		public void setErrorTemplate(String errorHandler) {
			CotopaxiConfigViewImpl.this.errorHandler = errorHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see br.octahedron.controller.ControllerConfig#setNotFoundHandler(java.lang.String)
		 */
		public void setNotFoundTemplate(String notFoundHandler) {
			CotopaxiConfigViewImpl.this.notFoundHandler = notFoundHandler;
		}

		@Override
		public void setTemplatesRoot(String templatesRoot) {
			CotopaxiConfigViewImpl.this.templatesRoot = templatesRoot;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * br.octahedron.controller.ControllerConfig#setCloudServicesFactory(br.octahedron.cloudservice
		 * .CloudServicesFactory)
		 */
		public void setCloudServicesFactory(CloudServicesFactory factory) {
			CotopaxiConfigViewImpl.this.factory = factory;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see br.octahedron.controller.ControllerConfig#addGlobalFilter(java.lang.Class)
		 */
		public void addGlobalFilter(Class<? extends Filter> filter) {
			if (CotopaxiConfigViewImpl.this.globalFilters == null) {
				CotopaxiConfigViewImpl.this.globalFilters = new LinkedList<Class<? extends Filter>>();
			}
			CotopaxiConfigViewImpl.this.globalFilters.add(filter);
		}

		@Override
		public void setRedirect(String originalURL, String destURL) {
			if (CotopaxiConfigViewImpl.this.redirects == null) {
				CotopaxiConfigViewImpl.this.redirects = new HashMap<String, String>();
			}
			CotopaxiConfigViewImpl.this.redirects.put(originalURL, destURL);
		}

		@Override
		public void setFormatter(String format, Class<? extends Formatter> formatterClass) {
			CotopaxiConfigViewImpl.this.formatters.put(format, formatterClass);
		}
	}
}
