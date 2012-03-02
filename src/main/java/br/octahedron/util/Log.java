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

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A simple Logger wrapper.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class Log {

	private Logger logger;
	private String name;

	private final static String getCallerClassName() {
		Thread t = Thread.currentThread();
		StackTraceElement[] trace = t.getStackTrace();
		StackTraceElement last = trace[3];
		return last.getClassName();
	}

	/**
	 * Create a new Log using the invoking class name as the log name.
	 */
	public Log() {
		this(getCallerClassName());
	}

	/**
	 * Create a new Log using the given class name.
	 */
	public Log(Class<?> klass) {
		this(klass.getName());
	}

	/**
	 * Creates a new Log with the given name.
	 */
	public Log(String name) {
		this.name = name;
		this.logger = Logger.getLogger(this.name);
	}

	/**
	 * @return This {@link Log} name
	 */
	public String getName() {
		return name;
	}

	protected void log(Level level, String message) {
		this.log(level, null, message);
	}

	protected void log(Level level, String format, Object... params) {
		this.log(level, null, String.format(format, params));
	}

	protected void log(Level level, Throwable t, String format, Object... params) {
		this.log(level, t, String.format(format, params));
	}

	protected void log(Level level, Throwable t, String message) {
		LogRecord record = new LogRecord(level, message);
		record.setSourceClassName(this.name);
		if (t != null) {
			record.setThrown(t);
		}
		this.logger.log(record);
	}

	/**
	 * debug level
	 */
	public void debug(String message) {
		this.log(FINEST, message);
	}

	/**
	 * debug level
	 */
	public void debug(String format, Object... params) {
		this.log(FINEST, format, params);
	}

	/**
	 * debug level
	 */
	public void debug(Throwable t, String message) {
		this.log(FINEST, t, message);
	}

	/**
	 * debug level
	 */
	public void debug(Throwable t, String format, Object... params) {
		this.log(FINEST, t, format, params);
	}

	/**
	 * info level
	 */
	public void info(String message) {
		this.log(INFO, message);
	}

	/**
	 * info level
	 */
	public void info(String format, Object... params) {
		this.log(INFO, format, params);
	}

	/**
	 * info level
	 */
	public void info(Throwable t, String message) {
		this.log(INFO, t, message);
	}

	/**
	 * info level
	 */
	public void info(Throwable t, String format, Object... params) {
		this.log(INFO, t, format, params);
	}

	/**
	 * warning level
	 */
	public void warning(String message) {
		this.log(WARNING, message);
	}

	/**
	 * warning level
	 */
	public void warning(String format, Object... params) {
		this.log(WARNING, format, params);
	}

	/**
	 * warning level
	 */
	public void warning(Throwable t, String message) {
		this.log(WARNING, t, message);
	}

	/**
	 * warning level
	 */
	public void warning(Throwable t, String format, Object... params) {
		this.log(WARNING, t, format, params);
	}

	/**
	 * error level
	 */
	public void error(String message) {
		this.log(SEVERE, message);
	}

	/**
	 * error level
	 */
	public void error(String format, Object... params) {
		this.log(SEVERE, format, params);
	}

	/**
	 * error level
	 */
	public void error(Throwable t, String message) {
		this.log(SEVERE, t, message);
	}

	/**
	 * error level
	 */
	public void error(Throwable t, String format, Object... params) {
		this.log(SEVERE, t, format, params);
	}
}
