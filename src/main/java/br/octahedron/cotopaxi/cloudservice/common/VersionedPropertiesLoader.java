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
package br.octahedron.cotopaxi.cloudservice.common;

import java.io.IOException;
import java.util.logging.Logger;

import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.inject.SelfInjector;
import br.octahedron.util.TestUtil;

/**
 * This class is responsible by load <code>VersionedProperties</code> entities. It caches properties
 * files using MemCache. When someone asks it to load a properties file, it checks the properties
 * file's version at MemCache and at disk and returns the most recent version, caching it.
 * 
 * To use VersionedProperties is necessary to set properties file's version at the System
 * Properties. The property should be named as the file, adding the ".version" suffix.
 * 
 * E.g.: If the configuration file is "gaecommons.properties", the properties file's version
 * property should be named "gaecommons.properties.version", and have the <b>version's number</b>.
 * To configure a SystemProperty, please visit {@link https
 * ://code.google.com/appengine/docs/java/config
 * /appconfig.html#System_Properties_and_Environment_Variables}.
 * 
 * Each time Properties File change, the version number should be updated. We recommend use the
 * 'epoch time' as the version number, to avoid colisions.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class VersionedPropertiesLoader extends SelfInjector {

	private static final String VERSIONING_SUFIX = ".version";

	private final Logger logger = Logger.getLogger(VersionedPropertiesLoader.class.getName());

	@Inject
	private MemcacheFacade memcacheFacade;

	/**
	 * @param memcacheFacade
	 *            Sets the memcacheFacade
	 */
	public void setMemcacheFacade(MemcacheFacade memcacheFacade) {
		this.memcacheFacade = memcacheFacade;
	}

	/**
	 * @param propertiesFileName
	 *            the properties' file name.
	 * @return the most recent version of the properties file.
	 * @throws IOException
	 *             If it needs to load properties from disk and an IO error occurs.
	 */
	public <T extends Enum<?>> VersionedProperties<T> getProperties(String propertiesFileName) throws IOException {
		long lastVersion = this.getLastVersionNumber(propertiesFileName);
		long memcacheVersion = Long.MIN_VALUE;

		VersionedProperties<T> properties = null;
		if ( ! TestUtil.isTestModeEnabled()) {
			/* Doesn't try to load from memcache when running on test mode */
			properties = this.loadFromMemcache(propertiesFileName);
		}
		if (properties != null) {
			// checks the memcache version number
			memcacheVersion = properties.getVersionNumber();
		} else {
			// theres no version at cache, so create a instance
			properties = new VersionedProperties<T>(propertiesFileName, lastVersion);
		}

		if (memcacheVersion < lastVersion) {
			// reload and cache the file if necessary
			properties.reload(lastVersion);
			this.saveToMemcache(properties);
		}

		return properties;
	}

	/**
	 * Gets the last version number, defined at appconfig.xml file.
	 */
	private long getLastVersionNumber(String propertiesFileName) {
		return Long.parseLong(System.getProperty(propertiesFileName + VERSIONING_SUFIX, "0"));
	}

	/**
	 * Loads a <code>VersionedProperties</code> from the cache.
	 */
	@SuppressWarnings("unchecked")
	private <T extends Enum<?>> VersionedProperties<T> loadFromMemcache(String propertiesFileName) {
		VersionedProperties<T> result = null;
		try {
			if (this.memcacheFacade.contains(propertiesFileName)) {
				result = this.memcacheFacade.get(VersionedProperties.class, propertiesFileName);
			}
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to load property file " + propertiesFileName + " from Memcache. Memcache Disabled!");
		}
		return result;
	}

	/**
	 * Saves a <code>VersionedProperties</code> to the cache.
	 */
	private <T extends Enum<?>> void saveToMemcache(VersionedProperties<T> properties) {
		try {
			this.memcacheFacade.put(properties.getFileName(), properties);
		} catch (DisabledMemcacheException e) {
			this.logger.warning("Unable to write property file " + properties.getFileName() + " to Memcache. Memcache Disabled!");
		}
	}
}
