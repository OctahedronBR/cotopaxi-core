package br.octahedron.cotopaxi.controller.adapters;

import br.octahedron.cotopaxi.model.InputAdapter;
import br.octahedron.cotopaxi.model.attribute.converter.RawStringConverter;

public class MappingGet extends InputAdapter {
	public MappingGet() {
		this.addAttribute("name", RawStringConverter.class);
	}
}