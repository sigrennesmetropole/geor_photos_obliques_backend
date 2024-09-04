/**
 *
 */
package org.georchestra.photosobliques.service.sm.common;

import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
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
}
