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
package br.octahedron.cotopaxi.i18n;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

import br.octahedron.cotopaxi.controller.ControllerDescriptor;

/**
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class LocaleManagerTest {
	
	private LocaleManager lcManager = new LocaleManager();
	
	@Test
	public void testOne() {
		ControllerDescriptor desc = new ControllerDescriptor("", "post", "Test", "br.octahedron");
		LocaleMap map = lcManager.getLocaleMap(desc, Arrays.asList(Locale.ITALIAN, Locale.FRANCE));
		assertEquals(2, map.size());
		assertEquals("World", map.get("WORLD"));
		assertEquals("verynice", map.get("NICE"));
		assertEquals("COOL", map.get("COOL"));
		
		desc = new ControllerDescriptor("", "post", "Test", "br.test");
		map = lcManager.getLocaleMap(desc, Arrays.asList(Locale.ITALIAN, Locale.FRANCE));
		assertEquals(1, map.size());
		assertEquals("verynice", map.get("NICE"));
		assertEquals("WORLD", map.get("WORLD"));
		assertEquals("COOL", map.get("COOL"));
		
		
		desc = new ControllerDescriptor("", "get", "Test", "br.octahedron");
		map = lcManager.getLocaleMap(desc, Arrays.asList(Locale.US));
		assertEquals(2, map.size());
		assertEquals("verynice", map.get("NICE"));
		assertEquals("World", map.get("WORLD"));
		assertEquals("Serviço", map.get("SERVICE"));
		assertEquals("Hi", map.get("Hi"));
	}

}
