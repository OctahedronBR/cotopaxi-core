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
package br.octahedron.cotopaxi.controller.adapters;

import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.converter.SafeStringConverter;
import br.octahedron.cotopaxi.model.attribute.validator.StringPatternValidator;
import br.octahedron.cotopaxi.model.attribute.validator.StringPatternValidator.CommonRegexs;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class AddModuleAdapter extends InputAdapter {

	public AddModuleAdapter() {
		this.addAttribute("event_name", SafeStringConverter.class, new StringPatternValidator(CommonRegexs.alfanumericAtLeastLength(4)));
		this.addAttribute("module_name", SafeStringConverter.class, new StringPatternValidator(CommonRegexs.alfanumericAtLeastLength(4)));
	}

}
