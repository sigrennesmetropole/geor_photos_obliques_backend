/**
 *
 */
package org.georchestra.photosobliques.service.sm.common;

import org.georchestra.photosobliques.core.common.Restrictable;
import org.georchestra.photosobliques.service.exception.AppServiceUnauthorizedException;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.service.helper.security.RestrictedHelper;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author FNI18300
 *
 */
@Slf4j
public abstract class AbstractRestrictedElementServiceImpl {

	@Autowired
	@Getter(value = AccessLevel.PROTECTED)
	private ACLHelper aclHelper;

	@Autowired
	@Getter(value = AccessLevel.PROTECTED)
	private RestrictedHelper restrictedHelper;

	protected void checkSecurity(Restrictable item) throws AppServiceUnauthorizedException {
		restrictedHelper.checkSecurity(item);
	}
}
