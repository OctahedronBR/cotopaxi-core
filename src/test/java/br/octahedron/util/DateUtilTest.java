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

import static br.octahedron.cotopaxi.CotopaxiProperty.TIMEZONE;
import static br.octahedron.util.DateUtil.defaultTimeZone;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.junit.Test;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 * TODO improve this tests
 */
public class DateUtilTest {
	
	private TimeZone tz1 = new SimpleTimeZone(-3*3600*1000, "America/Recife");
	private TimeZone tz2 = new SimpleTimeZone(+3*3600*1000, "America/Recife");
	private DateFormat format = new SimpleDateFormat("dd/MM/yyy HH:mm");

	@Test
	public void defaultTztest() {
		System.setProperty(TIMEZONE.name(), "-0800");
		assertEquals(-8*3600*1000, defaultTimeZone().getRawOffset());
	}
	
	@Test
	public void setDefault() {
		
	}
}
