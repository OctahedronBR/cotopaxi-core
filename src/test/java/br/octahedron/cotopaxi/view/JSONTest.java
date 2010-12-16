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
package br.octahedron.cotopaxi.view;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.octahedron.cotopaxi.model.auth.UserInfo;


/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class JSONTest {

	@Test
	public void testJSON1() {
		Map<String, Object> atts = new HashMap<String, Object>();
		atts.put("name", "Danilo");
//		Formatter fmt = new SimpleJSONFormatter(atts, Locale.US);
//		System.out.println(fmt.format());
		// TODO TEST!
	}
	
	@Test
	public void testJSON2() {
		Map<String, Object> atts = new HashMap<String, Object>();
		atts.put(UserInfo.USERNAME_ATTRIBUTE_NAME, new String("Danilo"));
		atts.put(UserInfo.USER_INFO_ATTRIBUTE, new UserInfo("Danilo", "Developer", "Tester"));
//		Formatter fmt = new SimpleJSONFormatter(atts, Locale.US);
//		System.out.println(fmt.format());
		// TODO TEST!
	}
}
