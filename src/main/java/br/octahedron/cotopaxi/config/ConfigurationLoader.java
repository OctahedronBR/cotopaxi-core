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
package br.octahedron.cotopaxi.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import br.octahedron.cotopaxi.Bootloader.Booter;
import br.octahedron.cotopaxi.config.ConfigurationParser.Token;
import br.octahedron.cotopaxi.config.ConfigurationParser.TokenType;
import br.octahedron.cotopaxi.controller.ControllerDescriptor;
import br.octahedron.cotopaxi.inject.DependencyManager;
import br.octahedron.cotopaxi.interceptor.InterceptorManager;
import br.octahedron.cotopaxi.route.Router;
import br.octahedron.util.FileUtil;
import br.octahedron.util.Log;

/**
 * Loads the configuration file
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public class ConfigurationLoader {

	private static final Log log = new Log(ConfigurationLoader.class);
	public static final String CONFIGURATION_FILENAME = "WEB-INF/application.config";
	private ConfigurationParser parser;
	private InterceptorManager interceptor;
	private Router router;
	private Booter booter;

	public ConfigurationLoader(Router router, InterceptorManager interceptor, Booter booter) throws FileNotFoundException {
		this.parser = new ConfigurationParser(FileUtil.getInputStream(CONFIGURATION_FILENAME));
		this.router = router;
		this.interceptor = interceptor;
		this.booter = booter;
	}

	protected ConfigurationLoader(Router router, InterceptorManager interceptor, InputStream in) {
		this.parser = new ConfigurationParser(in);
		this.router = router;
		this.interceptor = interceptor;
	}

	/**
	 * Loads configuration file from disk
	 */
	public void loadConfiguration() throws ConfigurationSyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			while (true) {
				Token tk = parser.nextToken();
				this.process(tk);
			}
		} catch (NoSuchElementException ex) {
			log.info("End of file reached.");
		}
	}

	private void process(Token tk) throws ConfigurationSyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			switch (tk.getTokenType()) {
			case CONTROLLERS:
				log.debug("%s block found", tk.getTokenType());
				this.processControllers(parser.nextToken());
				break;
			case INTERCEPTORS:
				log.debug("%s block found", tk.getTokenType());
				this.processInterceptors();
				break;
			case PROPERTIES:
				log.debug("%s block found", tk.getTokenType());
				this.processProperties();
				break;
			case DEPENDENCIES:
				log.debug("%s block found", tk.getTokenType());
				this.processDependencies();
				break;
			case BOOTLOADERS:
				log.debug("%s block found", tk.getTokenType());
				this.processBootloaders();
				break;
			default:
				throw new ConfigurationSyntaxException(tk);
			}
		} catch (UnexpectedTokenException ex) {
			this.process(ex.getToken());
		}
	}

	private void processDependencies() throws UnexpectedTokenException, ClassNotFoundException {
		do {
			String ifClass = this.getContent(TokenType.CLASS);
			String implClass = this.getContent(TokenType.CLASS);
			log.info("Configuration dependency found: %s -> %s", ifClass, implClass);
			DependencyManager.registerDependency(ifClass, implClass);
		} while (true);
	}

	private void processProperties() throws UnexpectedTokenException {
		do {
			String propertyName = this.getContent(TokenType.PROPERTY);
			String propertyValue = this.getContent(TokenType.STRING, TokenType.CLASS);
			log.info("Configuration property found: %s -> %s", propertyName, propertyValue);
			System.setProperty(propertyName, propertyValue);
		} while (true);
	}

	private void processInterceptors() throws UnexpectedTokenException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		do {
			String className = this.getContent(TokenType.CLASS);
			log.info("Configuration interceptor found: %s", className);
			interceptor.addInterceptor(className);
		} while (true);
	}

	private void processBootloaders() throws UnexpectedTokenException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		do {
			String bootloader = this.getContent(TokenType.CLASS);
			log.info("Configuration bootloader found: %s", bootloader);
			this.booter.addBootloader(bootloader);
		} while (true);
	}

	private void processControllers(Token tk) throws UnexpectedTokenException {
		if (tk.getTokenType() == TokenType.CLASS) {
			try {
				getControllerDescriptor(tk.getContent());
			} catch (UnexpectedTokenException ex) {
				this.processControllers(ex.getToken());
			}
		} else {
			throw new UnexpectedTokenException(tk);
		}
	}

	private void getControllerDescriptor(String controllerClass) throws UnexpectedTokenException {
		log.debug("Configuration controller found: %s", controllerClass);
		do {
			String url = this.getContent(TokenType.URL);
			String method = this.getContent(TokenType.STRING);
			String controllerName = this.getContent(TokenType.STRING);
			log.info("Adding controller descriptor %s - %s - %s - %s", controllerClass, url, method, controllerName);
			this.router.addRoute(new ControllerDescriptor(url, method, controllerName, controllerClass));
		} while (true);
	}

	private String getContent(TokenType... types) throws UnexpectedTokenException {
		Token tk = parser.nextToken();
		for (TokenType type : types) {
			if (tk.getTokenType() == type) {
				return tk.getContent();
			}
		}
		log.debug("Unexpected token type [%s, %s]", tk.getContent(), tk.getTokenType());
		throw new UnexpectedTokenException(tk);
	}

	/**
	 * Unexpected token exception
	 */
	class UnexpectedTokenException extends Exception {
		private static final long serialVersionUID = 1L;
		private Token token;

		public UnexpectedTokenException(Token tk) {
			this.token = tk;
		}

		public Token getToken() {
			return token;
		}
	}
}
