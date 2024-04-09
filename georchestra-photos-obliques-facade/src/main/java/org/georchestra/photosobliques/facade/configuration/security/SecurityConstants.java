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

	public static final String HEALTH_CHECK_URL = "/photosobliques/healthCheck";

	public static final String CONFIGURATION_URL = "/photosobliques/configuration";

	public static final String PHOTO_URL = "/photosobliques/photos";

}
