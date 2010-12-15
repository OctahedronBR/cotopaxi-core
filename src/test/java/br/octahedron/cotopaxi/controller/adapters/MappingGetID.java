package br.octahedron.cotopaxi.controller.adapters;

import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.converter.IntegerConverter;

public class MappingGetID extends InputAdapter {
	public MappingGetID() {
		this.addAttribute("id", IntegerConverter.class);
	}
}