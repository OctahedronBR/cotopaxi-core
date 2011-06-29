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
	private String sourceName;

	public Log(Class<?> klass) {
		this.sourceName = klass.getName();
		this.logger = Logger.getLogger(this.sourceName);
	}

	/**
	 * debug level
	 */
	public void debug(String message) {
		LogRecord record = new LogRecord(Level.FINEST, message);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}
	
	/**
	 * debug level
	 */
	public void debug(String format, Object ... params) {
		LogRecord record = new LogRecord(Level.FINEST, String.format(format, params));
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}

	/**
	 * debug level
	 */
	public void debug(String message, Throwable t) {
		LogRecord record = new LogRecord(Level.FINEST, message);
		record.setThrown(t);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}

	/**
	 * info level
	 */
	public void info(String message) {
		LogRecord record = new LogRecord(Level.INFO, message);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}

	/**
	 * info level
	 */
	public void info(String message, Throwable t) {
		LogRecord record = new LogRecord(Level.INFO, message);
		record.setThrown(t);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}
	
	/**
	 * info level
	 */
	public void info(String format, Object ... params) {
		LogRecord record = new LogRecord(Level.INFO, String.format(format, params));
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}
	
	/**
	 * warning level
	 */
	public void warning(String message) {
		LogRecord record = new LogRecord(Level.WARNING, message);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}

	/**
	 * warning level
	 */
	public void warning(String message, Throwable t) {
		LogRecord record = new LogRecord(Level.WARNING, message);
		record.setThrown(t);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}

	/**
	 * warning level
	 */
	public void warning(String format, Object ... params) {
		LogRecord record = new LogRecord(Level.WARNING, String.format(format, params));
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}
	
	/**
	 * error level
	 */
	public void error(String message) {
		LogRecord record = new LogRecord(Level.SEVERE, message);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}

	/**
	 * error level
	 */
	public void error(String message, Throwable t) {
		LogRecord record = new LogRecord(Level.SEVERE, message);
		record.setThrown(t);
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}
	
	/**
	 * error level
	 */
	public void error(String format, Object ... params) {
		LogRecord record = new LogRecord(Level.SEVERE, String.format(format, params));
		record.setSourceClassName(this.sourceName);
		this.logger.log(record);
	}
}
