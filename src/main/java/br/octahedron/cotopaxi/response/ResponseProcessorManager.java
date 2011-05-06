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
package br.octahedron.cotopaxi.response;

import java.util.LinkedList;
import java.util.List;


/**
 * @author Name - email@octahedron.com.br
 *
 */
public class ResponseProcessorManager implements ResponseProcessor {
	
	private List<ResponseProcessor> preRenderProcessors = new LinkedList<ResponseProcessor>();
	private ResponseProcessor render;
	private List<ResponseProcessor> preDeliverProcessors = new LinkedList<ResponseProcessor>();
	private ResponseProcessor deliver;

	public void addPreRenderProcessor(ResponseProcessor processor) {
		this.preRenderProcessors.add(processor);
	}

	public void addPreDeliverProcessor(ResponseProcessor processor) {
		this.preDeliverProcessors.add(processor);
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.controller.ResponseProcessor#process(br.octahedron.cotopaxi.controller.Response)
	 */
	@Override
	public void process(Response response) {
		// TODO Auto-generated method stub
		
	}

}
