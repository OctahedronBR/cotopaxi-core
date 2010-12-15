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
package br.octahedron.cotopaxi.view.i18n;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.util.FileUtil;

/**
 * A manager class to provide resources for a locale using a set of static strings from a property
 * file. It uses {@link MapResourceBundle} as the resource bundle and also store it in
 * {@link MemcacheFacade Memcache} to persuit a better performance. It has a
 * {@link #MASTER_FILENAME default} resource locale file that is always used in case of property is
 * not present in the requested resource bundle.
 * 
 * @see ResourceBundle
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class ResourceBundleManager {

	public final String I18N_SEPARATOR = "_";
	public final String I18N_EXTENSION = ".i18n";
	private final String MASTER_FILENAME = "master.i18n";

	private final Logger logger = Logger.getLogger(ResourceBundleManager.class.getName());
	private String i18nPath;
	private MemcacheFacade cache;

	public ResourceBundleManager(CloudServicesFactory factory, String i18nPath) {
		this.cache = factory.createMemcacheFacade();
		this.i18nPath = i18nPath;
	}

	/**
	 * Gets a resource bundle using the specified base name and locale.
	 * 
	 * @param baseName
	 *            The base name of the resource locale file.
	 * @param locale
	 *            The locale for which a resource bundle is desired.
	 * @return A resource bundle for the given base name and locale.
	 * @throws IOException If file not found, or if other IO error occurs. 
	 */
	public ResourceBundle getResourceBundle(String baseName, Locale locale) throws IOException {
		return this.getResourceBundle(this.i18nPath + baseName + this.I18N_SEPARATOR + locale.getCountry() + this.I18N_EXTENSION);

	}

	/**
	 * Gets a resource bundle using the specified file that is associeated with a specific locale.
	 * 
	 * @param filepath
	 *            The relative file path of property locale file.
	 * @return A resource bundle for the given base name and locale.
	 * @throws IOException
	 */
	protected ResourceBundle getResourceBundle(String filepath) throws IOException {
		Map<String, String> masterMap = this.getMapFromCache(this.i18nPath + this.MASTER_FILENAME);
		Map<String, String> mainMap = this.getMapFromCache(this.i18nPath + filepath);
		masterMap.putAll(mainMap);
		ResourceBundle resourceBundle = new MapResourceBundle(masterMap);

		return resourceBundle;
	}

	/**
	 * Gets a <code>Map<String, String></code> from memcache. If doesn't exist there, it will be
	 * loaded from a property file.
	 * 
	 * @param filepath
	 *            The relative file path of property locale file, also used as the key of resource
	 *            bundle in memcache.
	 * @return A <code>Map<String, String></code> containing the keys and values for a specific
	 *         locale or <code>null</code> if the file 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getMapFromCache(String filepath) throws FileNotFoundException, IOException {
		Map<String, String> map = null;
		try {
			map = this.cache.get(Map.class, filepath);
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to recover resourcebundle from Memcache. Memcache Disabled!");
		}

		if (map == null) {
			map = this.getMapFromProperties(filepath);
			try {
				this.cache.put(filepath, map);
			} catch (DisabledMemcacheException e) {
				this.logger.warning("Unable to put resourcebundle in Memcache. Memcache Disabled!");
			}
		}

		return map;
	}

	/**
	 * Loads a <code>Map<String, String></code> from a property file. It always called by
	 * {@link #getMapFromCache(String)} when a map is not in memcache.
	 * 
	 * @return A <code>Map<String, String></code> from a {@link Properties}.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getMapFromProperties(String filepath) throws FileNotFoundException, IOException {
			Properties props = new Properties();
			props.load(FileUtil.getInputStream(filepath));
			Map<String, String> map = new HashMap(props);
			return map;
		
	}

}
