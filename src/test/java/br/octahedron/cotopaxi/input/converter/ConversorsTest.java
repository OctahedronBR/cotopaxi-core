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
package br.octahedron.cotopaxi.input.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

import br.octahedron.cotopaxi.input.convert.ConversionException;
import br.octahedron.cotopaxi.input.convert.ConverterManager;
import br.octahedron.cotopaxi.input.convert.DateConverter;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ConversorsTest {

	private ConverterManager converter = ConverterManager.getInstance();

	@Test
	public void testDate() throws ConversionException {
		DateConverter.setDateFormat("dd/MM/yyyy");
		Date date1 = converter.convert(Date.class, "12/12/2012");
		Date date2 = converter.convert(Date.class, "13/12/2012");
		Date date3 = converter.convert(Date.class, "14/12/2012");

		assertEquals(-1, date1.compareTo(date2));
		assertEquals(-1, date1.compareTo(date3));
		assertEquals(1, date2.compareTo(date1));
		assertEquals(-1, date2.compareTo(date3));
		assertEquals(1, date3.compareTo(date1));
		assertEquals(1, date3.compareTo(date2));

	}

	@Test(expected = ConversionException.class)
	public void testDate1() throws ConversionException {
		converter.convert(Date.class, "yesterday");
	}

	@Test
	public void testBoolean() throws ConversionException {
		String a = "true";
		boolean result = converter.convert(Boolean.class, a);
		assertTrue(result);

		a = "TrUe";
		result = converter.convert(Boolean.class, a);
		assertTrue(result);

		a = "false";
		result = converter.convert(Boolean.class, a);
		assertFalse(result);

		a = "foo";
		result = converter.convert(Boolean.class, a);
		assertFalse(result);
	}

	@Test
	public void testInteger() throws ConversionException {
		int number = converter.convert(Integer.class, "191082");
		assertTrue(number == 191082);
		number = converter.convert(Integer.class, "-191082");
		assertTrue(number == -191082);
	}

	@Test(expected = ConversionException.class)
	public void testIntegerFail() throws ConversionException {
		converter.convert(Integer.class, "191082n");
	}

	@Test
	public void testLong() throws ConversionException {
		long number = converter.convert(Long.class, "191082");
		assertTrue(number == 191082);
		number = converter.convert(Long.class, "-191082");
		assertTrue(number == -191082);

	}

	@Test(expected = ConversionException.class)
	public void testLongFail() throws ConversionException {
		converter.convert(Long.class, "191082n");
	}

	@Test
	public void testDouble() throws ConversionException {
		double number = converter.convert(Double.class, "191082.0d");
		assertTrue(number == 191082);
		number = converter.convert(Double.class, "-191082.818281d");
		assertTrue(number == -191082.818281d);
	}

	@Test(expected = ConversionException.class)
	public void testDoubleFail() throws ConversionException {
		converter.convert(Double.class, "191082n");
	}

	@Test
	public void testFloat() throws ConversionException {
		float number = converter.convert(Float.class, "191082.0f");
		assertTrue(number == 191082);
		number = converter.convert(Float.class, "-191082.818281f");
		assertTrue(number == -191082.818281f);
	}

	@Test(expected = ConversionException.class)
	public void testFloatFail() throws ConversionException {
		converter.convert(Float.class, "191082n");
	}

	@Test
	public void testURL() throws ConversionException {
		URL url = converter.convert(URL.class, "http://www.octahedron.com.br");
		assertEquals("http://www.octahedron.com.br", url.toExternalForm());
	}

	@Test(expected = ConversionException.class)
	public void testURLFail() throws ConversionException {
		converter.convert(URL.class, "octahedron.com.br");
	}

	@Test
	public void testBigDecimal() throws ConversionException {
		converter.convert(BigDecimal.class, "1.666929292394");
	}
	
	@Test(expected = ConversionException.class)
	public void testBigDecimalFail() throws ConversionException {
		converter.convert(BigDecimal.class, "1.9.1");
	}

	@Test
	public void testCharacter() throws ConversionException {
		converter.convert(Character.class, "a");
	}
	@Test(expected = ConversionException.class)
	public void testCharacterFail() throws ConversionException {
		converter.convert(Character.class, "ab");
	}
}
