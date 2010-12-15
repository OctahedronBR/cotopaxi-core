package br.octahedron.cotopaxi.model;

import java.util.Map;

import br.octahedron.cotopaxi.view.TemplatesAttributes;

public interface ActionResponse {

	/**
	 * The available results from a model execution:
	 * 
	 * <pre>
	 *  * SUCCESS: indicates that model was executed successfully. The model execution result can be accessed 
	 *  using the key name set at the {@link Action} or using the {@link Class#getSimpleName()} if no
	 *  key was set;
	 *  * VALIDATION_FAILED: indicates that one or more input fields were invalid. The invalid fields can be accessed
	 *  using the attribute key {@link SuccessActionResponse#INVALIDATION_FIELDS_ATTRIBUTE};
	 *  * EXCEPTION: indicates that an exception has occurred executing the model. The following exception's attributes
	 *  can be accessed: {@link SuccessActionResponse#EXCEPTION_ATTRIBUTE} - the exception itself, {@link SuccessActionResponse#EXCEPTION_CLASS_ATTRIBUTE} -
	 *  the exception class, {@link SuccessActionResponse#EXCEPTION_MESSAGE_ATTRIBUTE} - the exception message and
	 *  {@link SuccessActionResponse#EXCEPTION_STACK_TRACE_ATTRIBUTE} - the exception stack trace, ready to be printed.
	 * </pre>
	 */
	public enum Result {
		SUCCESS, VALIDATION_FAILED, EXCEPTION;
	}

	/**
	 * Gets the ModelResponse Result type.
	 */
	public abstract Result getResult();

	/**
	 * Gets the attributes' {@link Map}
	 * 
	 * @return the attributes {@link Map}
	 */
	public Map<String, Object> getAttributes();

	/**
	 * Sets an attribute to this ModelResponse
	 * 
	 * @param key
	 *            The attribute key.
	 * @param value
	 *            The attribute value.
	 */
	public abstract void setAttribute(TemplatesAttributes key, Object value);

	/**
	 * Sets an attribute to this ModelResponse
	 * 
	 * @param key
	 *            The attribute key.
	 * @param value
	 *            The attribute value.
	 */
	public abstract void setAttribute(String key, Object value);

}