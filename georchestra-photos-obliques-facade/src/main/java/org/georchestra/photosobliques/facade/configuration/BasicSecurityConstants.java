/**
 *
 */
package org.georchestra.photosobliques.facade.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author FNI18300
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BasicSecurityConstants {

	public static final String SWAGGER_RESOURCES_URL = "/photosobliques/swagger-resources/**";

	public static final String SWAGGER_UI_HTML_URL = "/photosobliques/swagger-ui.html";

	public static final String SWAGGER_UI_URL = "/photosobliques/swagger-ui/**";

	public static final String V3_API_DOCS_URL = "/photosobliques/v3/api-docs/**";

	public static final String ACTUATOR_URL = "/actuator/**";

	public static final String ERROR_URL = "/error";

	public static final String WEBJARS_URL = "/webjars/**";

	public static final String CONFIGURATION_SECURITY_URL = "/configuration/security";

	public static final String CONFIGURATION_UI_URL = "/configuration/ui";

	public static final int SC_498 = 498;

}
