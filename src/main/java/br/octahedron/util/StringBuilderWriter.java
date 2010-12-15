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
package br.octahedron.util;

import java.io.Writer;

/**
 * A Writer that wraps the write operations to a StringBuilder. This class <b>IS NOT THREAD
 * SAFE</b>.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class StringBuilderWriter extends Writer {

	private final StringBuilder buffer = new StringBuilder();

	/**
	 * Gets this buffer.
	 */
	public StringBuilder getBuffer() {
		return this.buffer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#append(java.lang.CharSequence)
	 */
	@Override
	public Writer append(CharSequence csq) {
		this.buffer.append(csq);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#append(java.lang.CharSequence, int, int)
	 */
	@Override
	public Writer append(CharSequence csq, int start, int end) {
		this.buffer.append(csq, start, end);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#append(char)
	 */
	@Override
	public Writer append(char c) {
		this.buffer.append(c);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(int)
	 */
	@Override
	public void write(int c) {
		this.buffer.append(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(char[])
	 */
	@Override
	public void write(char[] cbuf) {
		this.buffer.append(cbuf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(char[] cbuf, int off, int len) {
		this.buffer.append(cbuf, off, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(java.lang.String)
	 */
	@Override
	public void write(String str) {
		this.buffer.append(str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#write(java.lang.String, int, int)
	 */
	@Override
	public void write(String str, int off, int len) {
		this.buffer.append(str.toCharArray(), off, len);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#flush()
	 */
	@Override
	public void flush() {
		// nothing to do

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Writer#close()
	 */
	@Override
	public void close() {
		// nothing to do
	}

}
