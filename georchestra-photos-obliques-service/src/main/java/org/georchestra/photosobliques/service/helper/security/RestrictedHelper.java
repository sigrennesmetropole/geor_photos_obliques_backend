/**
 *
 */
package org.georchestra.photosobliques.service.helper.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.common.Restrictable;
import org.georchestra.photosobliques.service.exception.AppServiceUnauthorizedException;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.springframework.stereotype.Component;

/**
 * @author FNI18300
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RestrictedHelper {

	private final ACLHelper aclHelper;

	public void checkSecurity(Restrictable item) throws AppServiceUnauthorizedException {
		// on controle d'abord les problématique d'habilitation
		if (item != null && !aclHelper.isRestricted() && item.isRestricted()) {
			log.error("[Security] {} try to access restricted data {}", aclHelper.getAuthenticatedUserLogin(),
					item.getUuid());
			throw new AppServiceUnauthorizedException(String.format("[Security] %s try to access restricted data",
					aclHelper.getAuthenticatedUserLogin()));
		}
	}
}
