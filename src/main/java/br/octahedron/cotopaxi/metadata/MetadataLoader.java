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
package br.octahedron.cotopaxi.metadata;

import java.lang.reflect.Method;

import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.util.reflect.AnnotatedMethod;
import br.octahedron.util.reflect.AnnotatedMethodListener;

/**
 * This class is a listener for annotated methods. It's responsible by load the mapping class and
 * <code>ModelMethod</code>s and configure the <code>Mapper</code> and <code>Executor</code>.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
class MetadataLoader implements AnnotatedMethodListener<Action> {

	private MetatadaMapper mapper;

	public MetadataLoader(MetatadaMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void annotatedMethodFound(AnnotatedMethod<Action> annMet) {
		Method met = annMet.getMethod();

		MetadataHandler entry = new MetadataHandler(met);
		this.mapper.add(entry);
	}
}