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
package br.octahedron.cotopaxi.view.formatter;

/**
 * Indicates that a usable {@link Formatter} for a specific format can't be created.
 * 
 * It can happens if the there's no {@link Formatter} registered for a given format, or if the
 * {@link Formatter} registered isn't valid, it means, it hasn't an empty constructor.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class FormatterNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public FormatterNotFoundException(String message) {
		super(message);
	}

	public FormatterNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
