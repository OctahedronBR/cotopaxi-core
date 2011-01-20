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
package br.octahedron.cotopaxi.metadata;

import java.lang.reflect.Method;

import br.octahedron.cotopaxi.metadata.annotation.Action;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired;
import br.octahedron.cotopaxi.metadata.annotation.Response;
import br.octahedron.cotopaxi.metadata.annotation.Template;
import br.octahedron.cotopaxi.metadata.annotation.Action.ActionMetadata;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired.LoginRequiredMetadata;
import br.octahedron.cotopaxi.metadata.annotation.Message.MessageMetadata;
import br.octahedron.cotopaxi.metadata.annotation.Response.ResponseMetadata;
import br.octahedron.cotopaxi.metadata.annotation.Template.TemplateMetadata;
import br.octahedron.cotopaxi.metadata.annotation.Redirect.RedirectMetadata;;

/**
 * This entity handlers all metadata extract {@link Action} methods.
 * 
 * @see Action
 * @see LoginRequired
 * @see Response
 * @see Template
 * @see Message
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class MetadataHandler {

	protected ActionMetadata actionMetadata;
	protected LoginRequiredMetadata loginMetadata;
	protected TemplateMetadata templateMetadata;
	protected ResponseMetadata responseMetadata;
	protected MessageMetadata messageMetadata;
	private RedirectMetadata redirectMetadata;

	protected MetadataHandler(Method met) {
		this.actionMetadata = new ActionMetadata(met);
		this.loginMetadata = new LoginRequiredMetadata(met);
		this.templateMetadata = new TemplateMetadata(met);
		this.responseMetadata = new ResponseMetadata(met);
		this.messageMetadata = new MessageMetadata(met);
		this.redirectMetadata = new RedirectMetadata(met);
	}

	public ActionMetadata getActionMetadata() {
		return this.actionMetadata;
	}

	public LoginRequiredMetadata getLoginMetadata() {
		return this.loginMetadata;
	}

	public TemplateMetadata getTemplateMetadata() {
		return this.templateMetadata;
	}

	public ResponseMetadata getResponseMetadata() {
		return this.responseMetadata;
	}

	public MessageMetadata getMessageMetadata() {
		return this.messageMetadata;
	}
	
	public RedirectMetadata getRedirectMetadata() {
		return redirectMetadata;
	}
}
