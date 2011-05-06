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
package br.octahedron.cotopaxi.request;

import java.util.LinkedList;
import java.util.List;

import br.octahedron.cotopaxi.response.ResponseProcessorManager;


/**
 * Process an request through the request processor chain.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public class RequestProcessorManager implements RequestProcessor {

	public enum ProcessingPhase {
		PRE_ROUTING, ROUTING, PRE_EXECUTION, EXECUTION;
	}

	private List<RequestProcessor> preRoutingProcessors = new LinkedList<RequestProcessor>();
	private RequestProcessor router;
	private List<RequestProcessor> preExecutionProcessors = new LinkedList<RequestProcessor>();
	private RequestProcessor executor;
	private ResponseProcessorManager responseProcessor;
	
	public void addPreRoutingProcessor(RequestProcessor processor) {
		this.preRoutingProcessors.add(processor);
	}

	public void addPreExecutionProcessor(RequestProcessor processor) {
		this.preExecutionProcessors.add(processor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.RequestProcessor#process(br.octahedron.cotopaxi.controller.Request)
	 */
	@Override
	public void process(Request request) {
		try {
			// execute pre-routing processors
			for (RequestProcessor processor : this.preRoutingProcessors) {
				processor.process(request);
			}
			// execute routing processor
			this.router.process(request);
			// execute pre-execution processor
			for (RequestProcessor processor : this.preExecutionProcessors) {
				processor.process(request);
			}
			// execute execution processor
			this.executor.process(request);
		} catch (NotFoundExeption ex) {
//			this.responseProcessor.404
		} catch (RequestProcessingException ex) {
			// TODO tratar exceção
		}
	}

}
