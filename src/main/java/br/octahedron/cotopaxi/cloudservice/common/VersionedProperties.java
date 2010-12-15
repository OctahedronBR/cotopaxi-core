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
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import br.octahedron.util.FileUtil;

/**
 * 
 * This class represents a persistent set of properties, that can be versioned. The version number
 * is used to cache this set of properties, avoiding load the properties from disk each time the
 * application restarts. The main goal of this is speed up the initialization of new applications
 * instances.
 * 
 * @see {@link Properties}
 * @see {@link VersionedPropertiesLoader}
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class VersionedProperties<T extends Enum<?>> implements Serializable {

	private static final long serialVersionUID = -5187933302052795369L;

	private long version;
	private String filename;
	private Properties properties;

	protected VersionedProperties(String propertyFileName, long version) {
		this.filename = propertyFileName;
		this.version = version;
		this.properties = new Properties();
	}

	public String getFileName() {
		return this.filename;
	}

	public String getProperty(T key) {
		return this.properties.getProperty(key.name());
	}

	protected void reload(long version) throws IOException {
		InputStream in = null;
		try {
			this.version = version;
			in = FileUtil.getInputStream(this.filename);
			this.properties.load(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public long getVersionNumber() {
		return this.version;
	}

}
