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
package br.octahedron.cotopaxi.model.attribute.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Date;

import org.junit.Test;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ConversorsTest {

	@Test
	public void testDate() throws ConversionException {
		DateConverter.setDateFormat("dd/MM/yyyy");
		DateConverter converter = new DateConverter();
		Date date1 = converter.convert("12/12/2012");
		Date date2 = converter.convert("13/12/2012");
		Date date3 = converter.convert("14/12/2012");

		assertEquals(-1, date1.compareTo(date2));
		assertEquals(-1, date1.compareTo(date3));
		assertEquals(1, date2.compareTo(date1));
		assertEquals(-1, date2.compareTo(date3));
		assertEquals(1, date3.compareTo(date1));
		assertEquals(1, date3.compareTo(date2));

	}

	@Test(expected = ConversionException.class)
	public void testDate1() throws ConversionException {
		DateConverter converter = new DateConverter();
		converter.convert("yesterday");
	}

	@Test
	public void testBoolean() {
		BooleanConverter converter = new BooleanConverter();
		String a = "true";
		boolean result = converter.convert(a);
		assertTrue(result);

		a = "TrUe";
		result = converter.convert(a);
		assertTrue(result);

		a = "false";
		result = converter.convert(a);
		assertFalse(result);

		a = "foo";
		result = converter.convert(a);
		assertFalse(result);
	}

	@Test
	public void testSafeString() throws ConversionException {
		String a = "<head><h1> test <br/><div id=\"teste\"/></h1>&nbsp;ra,ra!&gt;</head>";
		SafeStringConverter converter = new SafeStringConverter();
		String result = converter.convert(a);

		assertFalse(result.equals(a));
		assertEquals(result, " test ra,ra!");
	}

	@Test
	public void testRawString() throws ConversionException {
		String a = "test ra,ra!";
		RawStringConverter converter = new RawStringConverter();
		String result = converter.convert(a);
		assertEquals(result, a);
		assertEquals(result, "test ra,ra!");
	}

	@Test
	public void testStringArray() throws ConversionException {
		String a = "one beer,two beers ,three beers, fouurr beeeers!";
		StringCommaSeparatedArrayConverter converter = new StringCommaSeparatedArrayConverter();
		String[] result = converter.convert(a);
		assertTrue(result.length == 4);
		assertEquals("one beer", result[0]);
		assertEquals("two beers ", result[1]);
		assertEquals("three beers", result[2]);
		assertEquals(" fouurr beeeers!", result[3]);

	}

	@Test(expected = ConversionException.class)
	public void testInteger() throws ConversionException {
		IntegerConverter converter = new IntegerConverter();
		int number = converter.convert("191082");
		assertTrue(number == 191082);
		number = converter.convert("-191082");
		assertTrue(number == -191082);
		converter.convert("191082n");
	}

	@Test(expected = ConversionException.class)
	public void testLong() throws ConversionException {
		LongConverter converter = new LongConverter();
		long number = converter.convert("191082l");
		assertTrue(number == 191082);
		number = converter.convert("-191082l");
		assertTrue(number == -191082);
		converter.convert("191082n");
	}

	@Test(expected = ConversionException.class)
	public void testDouble() throws ConversionException {
		DoubleConverter converter = new DoubleConverter();
		double number = converter.convert("191082.0d");
		assertTrue(number == 191082);
		number = converter.convert("-191082.818281d");
		assertTrue(number == -191082.818281d);
		converter.convert("191082n");
	}

	@Test(expected = ConversionException.class)
	public void testFloat() throws ConversionException {
		FloatConverter converter = new FloatConverter();
		float number = converter.convert("191082.0f");
		assertTrue(number == 191082);
		number = converter.convert("-191082.818281f");
		assertTrue(number == -191082.818281f);
		converter.convert("191082n");
	}
	
	@Test(expected = ConversionException.class)
	public void testURL() throws ConversionException {
		URLConverter converter = new URLConverter();
		URL url = converter.convert("http://www.octahedron.com.br");
		assertEquals("http://www.octahedron.com.br", url.toExternalForm());
		url = converter.convert("octahedron.com.br");
	}
}
