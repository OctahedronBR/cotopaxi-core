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
package br.octahedron.cotopaxi.controller;

/**
 * This class handlers informations about the controller test
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class FiltersHelper {

	private static int filterBefore;
	private static int filterAfter;

	public static void reset() {
		filterAfter = 0;
		filterBefore = 0;
	}

	public static int getFilterAfter() {
		return filterAfter;
	}

	public static int getFilterBefore() {
		return filterBefore;
	}

	public static void filterBefore() {
		filterBefore++;
	}

	public static void filterAfter() {
		filterAfter++;
	}

}
