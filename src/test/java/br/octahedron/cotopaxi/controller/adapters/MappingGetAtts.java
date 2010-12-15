package br.octahedron.cotopaxi.controller.adapters;

import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.converter.IntegerConverter;
import br.octahedron.cotopaxi.model.attribute.converter.RawStringConverter;

public class MappingGetAtts extends InputAdapter {
	public MappingGetAtts() {
		this.addAttribute("name", RawStringConverter.class);
		this.addAttribute("id", IntegerConverter.class);
	}
}