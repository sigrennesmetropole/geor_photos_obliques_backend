/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade.configuration.security.permission;

import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.storage.entity.acl.FeatureScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Cet évaluator doit permettre de vérfifier que l'on a une feature avec un mode
 * particulier.<br/>
 * 
 * L'utilisation se fait comme suit : @PreAuthorize("hasPermission('code
 * featur','feature mode')")
 * 
 * @author FNI18300
 *
 */
@Component
public class FeaturePermissionEvaluator implements PermissionEvaluator {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeaturePermissionEvaluator.class);

	public static final String RESTRICTED = "restricted";

	@Override
	public boolean hasPermission(Authentication auth, Object feature, Object featureMode) {
		if ((auth == null) || (feature == null) || !(featureMode instanceof String)) {
			return false;
		}
		if (feature.equals(RESTRICTED)) {
			return hasRestricted(auth, Boolean.valueOf(featureMode.toString()));
		} else {
			return hasPrivilege(auth, feature.toString(), convertFeatureScope(featureMode.toString()));
		}
	}

	@Override
	public boolean hasPermission(Authentication auth, Serializable targetId, String feature, Object featureMode) {
		if ((auth == null) || (feature == null) || !(featureMode instanceof String)) {
			return false;
		}
		if (feature.equals(RESTRICTED)) {
			return hasRestricted(auth, Boolean.valueOf(featureMode.toString()));
		} else {
			return hasPrivilege(auth, feature, convertFeatureScope(featureMode.toString()));
		}
	}

	private FeatureScope convertFeatureScope(String featureMode) {
		try {
			return FeatureScope.valueOf(featureMode.toUpperCase());
		} catch (Exception e) {
			LOGGER.warn("Invalid feature mode:{}", featureMode);
			return null;
		}
	}

	private boolean hasRestricted(Authentication auth, boolean restrictedRequired) {
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getDetails();
		return authenticatedUser.isRestricted() == restrictedRequired;
	}

	private boolean hasPrivilege(Authentication auth, String targetType, FeatureScope targetFeatureScope) {
		AuthenticatedUser authenticatedUser = (AuthenticatedUser) auth.getDetails();
		FeatureScope authorizedFeatureScope = convertFeatureScope(authenticatedUser.getFeatureScope(targetType));
		if (targetFeatureScope == null || authorizedFeatureScope == null) {
			return false;
		} else {
			return targetFeatureScope.accept(authorizedFeatureScope);
		}
	}

}
