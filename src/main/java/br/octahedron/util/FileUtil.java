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
package br.octahedron.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 
 * Utility class to provide access to resource files
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class FileUtil {
	
	public static InputStream getInputStream(String filepath) throws FileNotFoundException {
		try {
			return new FileInputStream(getFile(filepath));
		} catch (FileNotFoundException ex) {
			InputStream in = ClassLoader.getSystemResourceAsStream(filepath);
			if (in != null) {
				return in;
			} else {
				throw ex;
			}
		}
	}

	public static File getFile(String filepath) {
		File f = new File(filepath);
		if (!f.exists()) {
			// for tests works! :-)
			f = new File("war", filepath);
		}
		return f;
	}

}
