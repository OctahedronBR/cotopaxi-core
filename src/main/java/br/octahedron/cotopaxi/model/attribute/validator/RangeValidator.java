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
package br.octahedron.cotopaxi.model.attribute.validator;

import java.util.Date;

/**
 * This class validates an object checking if this object is inside an given range.
 * 
 * To use this, the class type <code>T</code> should implements {@link Comparable}. Classes like
 * {@link Integer}, {@link Long}, {@link Double}, {@link Float}, {@link Date} implements
 * {@link Comparable} and can be used in this class.
 * 
 * Usage example:
 * 
 * <pre>
 * new RangeValidator&lt;Integer&gt;(new Integer(2), new Integer(4));
 * </pre>
 * 
 * @see Validator
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class RangeValidator<T> implements Validator<T> {

	private Comparable<T> max;
	private Comparable<T> min;
	private boolean inclusive;

	/**
	 * Its the same as call RangeValidator(min, max, false).
	 * 
	 * @see RangeValidator#RangeValidator(Comparable, Comparable, boolean)
	 */
	public RangeValidator(Comparable<T> min, Comparable<T> max) {
		this(min, max, false);
	}

	/**
	 * Creates a new RangeValidator. It will use the given parameters, min and max, to check if a
	 * value is inside this range. This check can be inclusive, or exclusive. If inclusive be
	 * <code>true</code> the evaluation will be 'min <= value <= max', if inclusive be false it will
	 * be 'min < value < max'.
	 * 
	 * Its also possible to determine only if the value is bigger than an minimum, or smaller than
	 * an maximum. In this case, you should provide the other extremity value as <code>null</code>.
	 * 
	 * @param min
	 *            the minimum range value, or <code>null</code> for a non-minimum range.
	 * @param max
	 *            the maximum range value, or <code>null</code> for a non-maximum range.
	 * @param inclusive
	 *            <code>true</code> if the evaluation is inclusive, <code>false</code> otherwise.
	 */
	public RangeValidator(Comparable<T> min, Comparable<T> max, boolean inclusive) {
		this.min = min;
		this.max = max;
		this.inclusive = inclusive;
	}

	@Override
	public boolean isValid(T object) {
		if (this.min != null) {
			/*
			 * We are comparing min to the object, so, min should be smaller (-1 | 0) than object
			 */
			int compare = this.min.compareTo(object);
			boolean valid = (this.inclusive) ? compare <= 0 : compare < 0;
			if (!valid) {
				return false;
			}
		}

		if (this.max != null) {
			/*
			 * We are comparing max to the object, so, max should be bigger (+1 | 0) than object
			 */
			int compare = this.max.compareTo(object);
			boolean valid = (this.inclusive) ? compare >= 0 : compare > 0;
			if (!valid) {
				return false;
			}
		}

		return true;
	}
}
