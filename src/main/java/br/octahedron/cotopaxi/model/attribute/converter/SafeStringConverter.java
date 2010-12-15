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

/**
 * This converter strips HTML codes from String.
 * 
 * @author Danilo Penna Queiroz - email@octahedron.com.br
 */
public class SafeStringConverter implements TypeConverter<String> {

	private static final String[] REGEXS = { "<[\\w =\"/]+>", "&[a-zA-Z]+;" };

	@Override
	public String convert(String strValue) throws ConversionException {
		if (strValue != null) {
			String result = strValue;
			for (String regex : REGEXS) {
				result = result.replaceAll(regex, "");
			}
			return result;
		} else {
			throw new ConversionException("String is null");
		}
	}
}
