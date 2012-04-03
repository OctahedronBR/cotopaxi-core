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
package br.octahedron.cotopaxi.view.render;

import static br.octahedron.cotopaxi.CotopaxiProperty.TEMPLATE_FOLDER;
import static br.octahedron.cotopaxi.CotopaxiProperty.charset;
import static br.octahedron.cotopaxi.CotopaxiProperty.property;
import static br.octahedron.util.FileUtil.getFile;
import static java.io.File.separator;

import java.io.File;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import br.octahedron.util.Log;

/**
 * VelocityTemplateRender is responsible for rendering velocity templates.
 * 
 * Generally used on controllers to render the attributes of request.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class VelocityTemplateRender implements TemplateRender {

	private static final Log log = new Log(VelocityTemplateRender.class);
	private static final String VELOCIMACRO_LIBRARY = "macros.vm";

	private final VelocityEngine engine = new VelocityEngine();

	public VelocityTemplateRender() {
		String templateFolder = property(TEMPLATE_FOLDER);
		if (!templateFolder.endsWith("/")) {
			templateFolder += '/';
		}
		Properties p = new Properties();
		p.setProperty("resource.loader", "file, class");
		p.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		p.setProperty("file.resource.loader.path", templateFolder);
		p.setProperty("input.encoding", charset().toString());
		p.setProperty("output.encoding", charset().toString());
		String macroFile = templateFolder + separator + VELOCIMACRO_LIBRARY;
		if (hasMacros(macroFile)) {
			p.setProperty("velocimacro.library", macroFile);
		}
		engine.init(p);
	}

	private static boolean hasMacros(String macroPath) {
		File f = getFile(macroPath);
		return f.exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.view.render.TemplateRender#render(java.lang.String,
	 * java.util.Map, java.io.Writer)
	 */
	public void render(String templatePath, Map<String, Object> output, Writer writer) {
		log.info("Rendering template %s", templatePath);
		VelocityContext context = new VelocityContext(output);
		Template template = engine.getTemplate(templatePath);
		template.merge(context, writer);
	}
}
