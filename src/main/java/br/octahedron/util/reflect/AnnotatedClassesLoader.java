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
package br.octahedron.util.reflect;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * Loads Annotated Classes.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class AnnotatedClassesLoader {

	private static final String CLASSES_FOLDER = "WEB-INF" + File.separatorChar + "classes";
	private static final String CLASS_SUFIX = ".class";

	/**
	 * Finds all classes annotated with <code>Action</code> annotation
	 */
	public static List<Class<?>> findAnnotatedClasses(String realPath, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {

		String path = realPath + File.separatorChar + CLASSES_FOLDER;
		File dir = new File(path);
		List<File> classFiles = getFiles(dir);
		List<Class<?>> annotatedClasses = new LinkedList<Class<?>>();

		for (File f : classFiles) {
			if (!f.getAbsolutePath().endsWith(CLASS_SUFIX)) {
				continue;
			}
			Class<?> ant = Class.forName(getBinaryName(f, dir));
			if (ant.isAnnotationPresent(annotationClass)) {
				annotatedClasses.add(ant);
			}
		}
		return annotatedClasses;
	}

	/**
	 * Gets the binary name of the given file. Eg.: foo.bar.Test
	 */
	private static String getBinaryName(File f, File baseDir) {
		String[] tokens = f.getAbsolutePath().split("/");
		String[] path = baseDir.getAbsolutePath().split("/");

		int count = 0;
		StringBuffer out = new StringBuffer();

		for (String str : tokens) {
			if (count < path.length) {
				if (str.equals(path[count])) {
					count++;
					continue;
				}
			}

			out.append(str);
			out.append(".");
		}

		out.deleteCharAt(out.length() - 1);
		out.delete(out.lastIndexOf("."), out.length());

		return out.toString();
	}

	/**
	 * Returns a list with all ".class" files founded int the given dir and its subdirectories.
	 */
	private static List<File> getFiles(File dir) {
		List<File> files = new LinkedList<File>();
		File[] childs = dir.listFiles();
		for (File f : childs) {
			if (f.isDirectory()) {
				files.addAll(getFiles(f));
			} else {
				files.add(f);
			}
		}
		return files;
	}

}
