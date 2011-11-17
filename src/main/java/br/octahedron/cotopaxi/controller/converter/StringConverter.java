package br.octahedron.cotopaxi.controller.converter;

import br.octahedron.cotopaxi.controller.Converter;
import br.octahedron.cotopaxi.controller.ConvertionException;

/**
 * Convenience converter that simply returns the same String, useful for
 * hierarchies or code paths where you are required to use a converter 
 * 
 * @author thiagoss
 *
 */
public class StringConverter implements Converter<String> {

	@Override
	public String convert(String input) throws ConvertionException {
		return input;
	}

}
