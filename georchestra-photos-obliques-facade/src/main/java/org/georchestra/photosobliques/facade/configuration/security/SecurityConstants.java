/**
 *
 */
package org.georchestra.photosobliques.facade.configuration.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author FNI18300
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {

	public static final String GEOSERVER_URL = "/geoserver/**";

	public static final String LOGOUT_URL = "/api/v1/users/logout";

	public static final String AUTHENTICATE_URL = "/api/v1/users/authenticate";

	public static final String AUTHENTICATED_URL = "/api/v1/users/authenticated";

	public static final String HEALTH_CHECK_URL = "/api/v1/healthCheck";

	public static final String CONFIGURATION_URL = "/api/v1/configuration";

}
