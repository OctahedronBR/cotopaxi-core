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
package br.octahedron.cotopaxi.controller;

import static br.octahedron.cotopaxi.model.auth.UserInfo.USER_INFO_ATTRIBUTE;
import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.config.CotopaxiConfigView;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.metadata.MetadataHandler;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired.LoginRequiredMetadata;
import br.octahedron.cotopaxi.model.ActionResponse;
import br.octahedron.cotopaxi.model.SuccessActionResponse;
import br.octahedron.cotopaxi.model.auth.UserInfo;
import br.octahedron.util.ThreadProperties;

/**
 * The Cotopaxi Controller. This is application/requests entry point.
 * 
 * Its responsible by all the application workflow. At startup, the controller loads the ModelFacade
 * and configures the controller, and on each request, it maps the request, prepares the parameters,
 * executes the the model method, the filters, and process the response.
 * 
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class ControllerManager {

	/*
	 * ** Controller/Model Initialization Workflow
	 * 
	 * On servlet initialization, the controler creates the ModelConfigurator and the
	 * ControllerConfiguration. Then, it passes the ControllerConfiguration for the
	 * ModelConfigurator (see ModelConfigurator#configure). Its important to note that after this
	 * the ControllerConfiguration isn't mutable anymore, and all other classes will deal with a
	 * ControllerConfigurationView, an immutable version of the configuration.
	 * 
	 * After that, the mapper and the executor are created, and a passed to a
	 * ModelFacadeMetadataLoader. The ModelFacadeMetadataLoader is responsible by check all
	 * ModelFacade's annotated methods, and load and configure the corresponded ModelMappings.
	 * 
	 * ** Request Process Workflow
	 * 
	 * When the controller receive a request, first it ask the mapper for a ModelMapping for the
	 * request URL (see the Mapper for the mapping workflow). If a ModelMapping can't be found for
	 * the given URL, it results on a 404 response. However, if the ModelMapping is found, a
	 * RequestHandler is created and both the mapping as the request handler are passed to the
	 * executor, that is responsible by process the mapping/request (it means, execute filter,
	 * convert and validate input, execute model method, generate response and process response).
	 * See the Executor for a detailed workflow.
	 */

	private ModelController modelController;

	/**
	 * Creates and setup the controller.
	 * 
	 * Creates the configurator, configure it, starts the Mapper and the executor, loads all the
	 * meta-information about model methods.
	 */
	public ControllerManager(CotopaxiConfigView config) {
		// create the executor
		this.modelController = new ModelController(config);
	}

	/**
	 * Checks the permissions for the given {@link RequestWrapper}/{@link MetadataHandler}, check
	 * format, execute the {@link ModelController} and return the {@link SuccessActionResponse}.
	 */
	public ActionResponse process(RequestWrapper request, MetadataHandler metadata) throws AuthorizationException, FilterException,
			IllegalArgumentException, IllegalAccessException {
		/*
		 * Check permissions, check formats pass to model controller to execute return response
		 */
		this.checkPermissions(request, metadata);
		return this.modelController.executeRequest(request, metadata.getActionMetadata());
	}

	private void checkPermissions(RequestWrapper request, MetadataHandler metadata) throws AuthorizationException {
		// checks if the model method has login restrictions
		LoginRequiredMetadata loginMetadata = metadata.getLoginMetadata();
		if (loginMetadata.isLoginRequired()) {
			// gets the current user
			UserInfo user = (UserInfo) ThreadProperties.getProperty(USER_INFO_ATTRIBUTE);
			if (user != null) {
				String username = user.getUsername();
				request.setRequestParameter(UserInfo.USERNAME_ATTRIBUTE_NAME, username);
				if (!user.satisfyRole(loginMetadata.getRequiredRole())) {
					throw new AuthorizationException("The user " + username + " doesn't has the required role: " + loginMetadata.getRequiredRole(),
							loginMetadata.getForbiddenURL());
				}
			} else {
				throw new AuthorizationException("There's no user logged at current session.", loginMetadata.getLoginURL());
			}
		}
	}
}
