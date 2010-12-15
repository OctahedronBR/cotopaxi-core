package br.octahedron.cotopaxi.model.attribute;

import br.octahedron.cotopaxi.InputHandler;
import br.octahedron.cotopaxi.model.attribute.converter.ConversionException;

public interface ModelAttribute<T> {

	public abstract String getName();

	public abstract T getAttributeValue(InputHandler input) throws InvalidAttributeException, ConversionException;

}