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
package br.octahedron.cotopaxi.input;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Encapsulates the model's input parameters, extracted from a request.
 * 
 * This parameters will be passed to model in the order in which parameters value was inserted.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ModelInput {
	
	private LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
	
	/**
	 * Adds an parameter and its value to the model input.
	 * @param paramName The parameter's name
	 * @param paramValue The parameter's value
	 */
	public void addParameter(String paramName, Object paramValue) {
		this.parameters.put(paramName, paramValue);
	}
	
	public Object getParamValue(String paramName) {
		return this.parameters.get(paramName);
	}
	
	public Object[] getModelParameters() {
		Object[] params = new Object[this.parameters.size()];
		int i = 0;
		for(Entry<String, Object> entry : this.parameters.entrySet()) {
			params[i] = entry.getValue();
			i++;
		}
		return params;
	}
}
