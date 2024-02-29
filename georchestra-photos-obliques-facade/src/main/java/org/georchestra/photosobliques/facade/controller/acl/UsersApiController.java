/**
 *
 */
package org.georchestra.photosobliques.facade.controller.acl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.bean.Tokens;
import org.georchestra.photosobliques.core.bean.User;
import org.georchestra.photosobliques.facade.configuration.BasicSecurityConstants;
import org.georchestra.photosobliques.facade.configuration.security.cache.AccessTokenManager;
import org.georchestra.photosobliques.facade.util.AbstractController;
import org.georchestra.photosobliques.service.sm.acl.UserService;
import org.georchestra.photosobliques.facade.controller.api.UsersApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FNI18300
 *
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class UsersApiController extends AbstractController implements UsersApi {

	private final UserService userService;

	private final HttpServletResponse servletResponse;

	private final AccessTokenManager accessTokenManager;

	@Override
	public ResponseEntity<User> getMe() {
		return ResponseEntity.ok(userService.getMe());
	}

	@Override
	public ResponseEntity<Boolean> isAuthenticated() throws Exception {
		return ResponseEntity.ok(userService.getMe() != null);
	}

	@Override
	public ResponseEntity<Tokens> authenticate(String token) throws Exception {
		org.georchestra.photosobliques.facade.configuration.filter.Tokens tokens = accessTokenManager.lookupTokens(token);
		if (tokens != null) {
			Tokens result = new Tokens().jwt(tokens.getJwtToken()).xToken(tokens.getRefreshToken());
			return ResponseEntity.status(HttpStatus.OK)
					.header(BasicSecurityConstants.HEADER_AUTHENTIZATION_KEY, result.getJwt())
					.header(BasicSecurityConstants.HEADER_REFRESH_TOKEN_KEY, result.getxToken()).body(result);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@Override
	public ResponseEntity<Tokens> refreshToken() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Refresh token done");
		}
		Tokens tokens = new Tokens();
		tokens.setJwt(servletResponse.getHeader(BasicSecurityConstants.HEADER_AUTHENTIZATION_KEY));
		tokens.setxToken(servletResponse.getHeader(BasicSecurityConstants.HEADER_REFRESH_TOKEN_KEY));
		return ResponseEntity.ok(tokens);
	}

	/**
	 * @see
	 */
	@Override
	public ResponseEntity<String> logout() throws Exception {
		// nothing to do
		return ResponseEntity.ok("Goodbye!");
	}

}
