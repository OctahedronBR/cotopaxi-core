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
package br.octahedron.cotopaxi;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.controller.FacadeThree;
import br.octahedron.cotopaxi.controller.auth.AuthManager;
import br.octahedron.cotopaxi.controller.auth.UserInfo;
import br.octahedron.cotopaxi.controller.auth.UserLookupStrategy;
import br.octahedron.cotopaxi.controller.auth.UserNotAuthorizedException;
import br.octahedron.cotopaxi.controller.auth.UserNotLoggedException;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.metadata.MetadataMapper;
import br.octahedron.cotopaxi.metadata.PageNotFoundExeption;
import br.octahedron.cotopaxi.metadata.annotation.Action.HTTPMethod;
import br.octahedron.cotopaxi.metadata.annotation.LoginRequired.LoginRequiredMetadata;

/**
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class AuthTest {

	private UserLookupStrategy userStrategy;
	private CotopaxiConfigView config = CotopaxiConfigView.getInstance();
	private AuthManager auth;
	private MetadataMapper mapper;

	@Before
	public void setUp() throws SecurityException, NoSuchMethodException {
		this.userStrategy = createMock(UserLookupStrategy.class);
		this.config.getCotopaxiConfig().setUserLookupStrategy(this.userStrategy);
		this.config.getCotopaxiConfig().addModelFacade(FacadeThree.class);
		this.auth = new AuthManager(this.config);
		this.mapper = new MetadataMapper(this.config);
	}

	@Test
	public void authenticationTest1() throws IllegalArgumentException, FilterException, IllegalAccessException, PageNotFoundExeption,
			UserNotAuthorizedException {
		/*
		 * This test checks no logged user
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);
		expect(this.userStrategy.getCurrentUSer()).andReturn(null);
		expect(this.userStrategy.getLoginURL("/restricted1")).andReturn("/login");
		replay(this.userStrategy);

		try {
			// invoking the auth mechanism
			LoginRequiredMetadata login = this.mapper.getMapping(request).getLoginMetadata();
			this.auth.authorizeUser(request, login);
			fail();
		} catch (UserNotLoggedException e) {
			assertEquals("/login", e.getRedirectURL());
		} finally {
			// check test results
			verify(request);
			verify(this.userStrategy);
		}
	}

	@Test
	public void authenticationTest2() throws PageNotFoundExeption, UserNotLoggedException, UserNotAuthorizedException {
		/*
		 * This test an logged user
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted1").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);
		expect(this.userStrategy.getCurrentUSer()).andReturn(new UserInfo("danilo"));
		replay(this.userStrategy);

		// invoking the auth mechanism
		LoginRequiredMetadata login = this.mapper.getMapping(request).getLoginMetadata();
		this.auth.authorizeUser(request, login);

		// check test results
		verify(request);
		verify(this.userStrategy);
	}

	@Test(expected = UserNotAuthorizedException.class)
	public void authenticationTest3() throws PageNotFoundExeption, UserNotLoggedException, UserNotAuthorizedException {
		/*
		 * This test an logged user but with wrong role
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted3").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);
		expect(this.userStrategy.getCurrentUSer()).andReturn(new UserInfo("danilo", "tester"));
		replay(this.userStrategy);

		try {
			// invoking the auth mechanism
			LoginRequiredMetadata login = this.mapper.getMapping(request).getLoginMetadata();
			this.auth.authorizeUser(request, login);
		} finally {
			// check test results
			verify(request);
			verify(this.userStrategy);
		}
	}

	@Test
	public void authenticationTest4() throws PageNotFoundExeption, UserNotLoggedException, UserNotAuthorizedException {
		/*
		 * This test an logged with required role
		 */
		// Prepare test
		RequestWrapper request = createMock(RequestWrapper.class);
		expect(request.getURL()).andReturn("/restricted3").atLeastOnce();
		expect(request.getHTTPMethod()).andReturn(HTTPMethod.GET).atLeastOnce();
		expect(request.getFormat()).andReturn(null);
		replay(request);
		expect(this.userStrategy.getCurrentUSer()).andReturn(new UserInfo("danilo", "admin"));
		replay(this.userStrategy);

		// invoking the auth mechanism
		LoginRequiredMetadata login = this.mapper.getMapping(request).getLoginMetadata();
		this.auth.authorizeUser(request, login);

		// check test results
		verify(request);
		verify(this.userStrategy);
	}

}
