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
package br.octahedron.cotopaxi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author Danilo Queiroz - dpenna.queiroz
 */
public class FullControllerTest extends CotopaxiTestHelper {

	
	@Test
	public void executeController1() {
		Request req = request("/test", "get");
		req.authorized();
		req.currentUser("dpenna.queiroz@gmail.com");
		req.serverName("www.example.com");
		req.in("name", "world");
		
		Response resp = process(req);
		
		assertEquals(200, resp.code());
		assertTrue(resp.isTemplate());
		assertFalse(resp.isJSON());
		assertFalse(resp.isRedirect());
		assertEquals("get.vm", resp.template());
		assertTrue(resp.existsOutput("name"));
		assertEquals("world", resp.output("name"));
		assertTrue(resp.existsOutput("msg"));
		assertEquals("hello world", resp.output("msg"));
		assertTrue(resp.existsOutput("user"));
		assertEquals("dpenna.queiroz@gmail.com", resp.output("user"));
	}
	
	@Test
	public void executeController2() {
		Request req = request("/testo", "get");
		
		Response resp = process(req);
		
		assertEquals(404, resp.code());
		assertTrue(resp.isTemplate());
		assertFalse(resp.isJSON());
		assertFalse(resp.isRedirect());
	}
	
	@Test
	public void executeController3() {
		Request req = request("/test", "get");
		Response resp = process(req);
		assertTrue(resp.isRedirect());
		assertEquals("/mimi", resp.redirect());
	}
	
	@Test
	public void executeController4() {
		Request req = request("/test/lalala", "post");
		req.authorized();
		req.currentUser("dpenna.queiroz@gmail.com");
		req.serverName("www.example.com");
		
		Response resp = process(req);
		
		assertEquals(200, resp.code());
		assertTrue(resp.isJSON());
		assertFalse(resp.isTemplate());
		assertFalse(resp.isRedirect());
		assertTrue(this.onSession("something"));
		assertEquals("hey", this.session("something"));
		assertFalse(resp.existsOutput("name"));
		assertTrue(resp.existsOutput("msg"));
		assertEquals("hello noob", resp.output("msg"));
		assertTrue(resp.existsOutput("user"));
		assertEquals("dpenna.queiroz@gmail.com", resp.output("user"));
	}
	
	@Test
	public void executeController5() {
		Request req = request("/test/lalala", "post");
		req.currentUser("dpenna.queiroz@gmail.com");
		req.serverName("www.example.com");
		
		Response resp = process(req);
		
		assertEquals(500, resp.code());
		assertTrue(resp.isTemplate());
		assertFalse(resp.isJSON());
		assertFalse(resp.isRedirect());
		assertEquals("error.vm", resp.template());
		assertTrue(resp.existsOutput("error"));
	}
}
