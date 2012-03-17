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
package br.octahedron.cotopaxi.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import br.octahedron.cotopaxi.config.ConfigurationSyntaxException;
import br.octahedron.cotopaxi.controller.ControllerResponse;

/**
 * This class provides mechanism create Tests to controller classes.
 * 
 * It helps you to simulate requests to be processed and executed by work controllers and check the
 * given responses.
 * 
 * It simulates the framework by loading you configuration file, routing your request and given it
 * to the controller executor. The controller executor will executes your controller and returns the
 * response.
 * 
 * It executes the entire framework controller stack, including the controller, template and
 * finalizer interceptors.
 * 
 * The response dispatchers aren't executed, though.
 * 
 * This way, this class provides methods to manipulate session, create requests, and do dispatch
 * request to be process.
 * 
 * It also provides some useful methods to deal with Mock objects, for complex configuration,
 * including database calls and others.
 * 
 * See a simple example bellow:
 * 
 * <pre>
 * import static org.junit.Assert.assertEquals;
 * import static org.junit.Assert.assertFalse;
 * import static org.junit.Assert.assertTrue;
 * 
 * import org.junit.Test;
 * 
 * public class SimpleTest extends CotopaxiTestHelper {
 * 
 * 	&#064;Test
 * 	public void firstTest() {
 * 		// request setup
 * 		Request req = this.request(&quot;/some/url&quot;, &quot;get&quot;);
 * 		req.in(&quot;name&quot;, &quot;world&quot;);
 * 		req.serverName(&quot;www.example.com&quot;);
 * 		req.currentUser(&quot;dpenna.queiroz@gmail.com&quot;);
 * 		req.authorized();
 * 
 * 		// process request and get the response
 * 		Response resp = this.process(req);
 * 
 * 		// check response
 * 		assertEquals(200, resp.code());
 * 		assertTrue(resp.isTemplate());
 * 		assertFalse(resp.isJSON());
 * 		assertFalse(resp.isRedirect());
 * 		assertEquals(&quot;template.vm&quot;, resp.template());
 * 		assertTrue(resp.existsOutput(&quot;some-output&quot;));
 * 		assertEquals(&quot;expected&quot;, resp.output(&quot;some-output&quot;));
 * 	}
 * }
 * </pre>
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class CotopaxiTestHelper {

	/**
	 * Gets the HttpSession for this Test Environment
	 * 
	 * @return The current HttpSession
	 */
	protected final static HttpSession session() {
		return new MapHttpSession(session);
	}

	private static final InternalTestServlet servlet = InternalTestServlet.getInstance();
	private static final ArrayList<Object> mocks = new ArrayList<Object>();
	private static final Map<String, Object> session = new HashMap<String, Object>();

	/**
	 * Prepares this TestCase class to be executed.
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws ConfigurationSyntaxException
	 * @throws FileNotFoundException
	 */
	@BeforeClass
	public static final void classSetup() throws ServletException, FileNotFoundException, ConfigurationSyntaxException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		mocks.clear();
		session.clear();
		servlet.forceReload();
	}

	/**
	 * Reset environment.
	 */
	@AfterClass
	public static final void classDestroy() {
		servlet.forceReset();
	}

	/**
	 * Cleans the house after a Test method execution.
	 */
	@After
	public final void testCleaner() {
		EasyMock.reset(mocks.toArray());
	}

	/**
	 * Creates a mock for the given class and register for TestCase class.
	 * 
	 * If testing controllers using the {@link CotopaxiTestHelper#process(Request)} method,
	 * registered mocks are automatic managed by this TestCase.
	 * 
	 * In case of need to manage mocks directly, you can do it by using
	 * {@link CotopaxiTestHelper#replay()} and {@link CotopaxiTestHelper#verify()} methods.
	 * 
	 * In all the cases, the mocks are always reseted after a Test method execution.
	 * 
	 * @see EasyMock#reset(Object...)
	 * 
	 * @param mockClass
	 *            The class to be Mocked.
	 * @return A registered mock.
	 */
	public final <T> T createMock(Class<T> mockClass) {
		T mock = createMock(mockClass);
		mocks.add(mock);
		return mock;
	}

	/**
	 * Resets the session, removing all existing objects.
	 */
	protected final void clearSession() {
		session.clear();
	}

	/**
	 * Resets the session, removing all existing objects.
	 */
	protected final void clearMocks() {
		mocks.clear();
	}

	/**
	 * Replay all registered mocks
	 * 
	 * @see EasyMock#replay(Object...)
	 */
	protected final void replay() {
		EasyMock.replay(mocks.toArray());
	}

	/**
	 * Verify all registered mocks
	 * 
	 * @see EasyMock#verify(Object...)
	 */
	protected final void verify() {
		EasyMock.verify(mocks.toArray());
	}

	/**
	 * Adds an object to current session
	 * 
	 * @param key
	 *            The object's key
	 * @param value
	 *            The object itself
	 */
	protected final void session(String key, Object value) {
		session.put(key, value);
	}

	/**
	 * Gets an Object from session.
	 * 
	 * @param key
	 *            The object's key
	 * @return The object retrieved from session, or <code>null</code> if there's no such object on
	 *         session.
	 */
	protected final Object session(String key) {
		return session.get(key);
	}

	/**
	 * Checks if an object is on session
	 * 
	 * @param key
	 *            The object's key
	 * @return <code>true</code> if the object is on session, <code>false</code> otherwise.
	 */
	protected final boolean onSession(String key) {
		return session.containsKey(key);
	}

	/**
	 * Creates a new request for the given url, using the given http method.
	 * 
	 * @param url
	 *            The request's relative url. Eg.: "/dashboard"
	 * @param method
	 *            The http method. Should be one of GET, POST, PUT, DELETE
	 * @return The request for the given url and method.
	 */
	protected final Request request(String url, String method) {
		return new Request(url, method);
	}

	/**
	 * Dispatch a request to be processed. It will executed all the framework stack for request
	 * processing: routing, controller interceptors, controller, template interceptor and finalizer
	 * interceptor.
	 * 
	 * It will perform the response dispatch operation.
	 * 
	 * @param req
	 *            The request to be processed.
	 * @return The {@link Response} for the request.
	 */
	protected final Response process(Request req) {
		try {
			// replay mocks
			this.replay();
			// execute controller
			servlet.deliver(req.servletRequest(), null);
			ControllerResponse resp = servlet.getLastResponse();
			// verify mocks
			this.verify();
			// return response
			return new Response(resp);
		} catch (ServletException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
