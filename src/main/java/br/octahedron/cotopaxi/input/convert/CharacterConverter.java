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
package br.octahedron.cotopaxi.input.convert;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class CharacterConverter implements TypeConverter<Character> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cotopaxi.input.convert.TypeConverter#convert(java.lang.String)
	 */
	@Override
	public Character convert(String strValue) throws ConversionException {
		strValue = strValue.trim();
		if (strValue.length() == 1) {
			return strValue.charAt(0);
		} else {
			throw new ConversionException("Cannot convert the given java.lang.String to java.lang.Character");
		}
	}
}
