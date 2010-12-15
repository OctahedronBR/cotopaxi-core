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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.jdo.annotations.PrimaryKey;

import org.junit.Test;

import br.octahedron.cotopaxi.config.CotopaxiConfigurator;
import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.model.InputAdapter;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class ReflectionUtilTest {

	@Test
	public void testReflection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException,
			InvocationTargetException {
		CotopaxiConfigurator facade = (CotopaxiConfigurator) ReflectionUtil.createInstance("br.octahedron.util.reflect.FakeObj");

		Collection<AnnotatedMethod<Action>> methods = ReflectionUtil.getAnnotatedMethods(FakeObj.class, Action.class);
		assertEquals(1, methods.size());
		AnnotatedMethod<Action> annMet = methods.iterator().next();
		assertEquals("test", annMet.getMethod().getName());
		assertEquals(InputAdapter.class, annMet.getAnnotation().adapter());

		assertTrue(((Boolean) ReflectionUtil.invoke(facade, annMet.getMethod(), new Object[0])));
	}

	@Test
	public void testReflection2() {
		Object obj = new FakeObj();

		Object value = ReflectionUtil.getAnnotatedFieldValue(obj, PrimaryKey.class);
		assertEquals("key", value);
	}
}
