package org.georchestra.photosobliques.service.helper.acl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Service utilitaire pour récupérer les infos sur l'utilisateur connecté.
 */
@Component
public class ACLHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ACLHelper.class);

	/**
	 * Retourne l'utilisateur connecté.
	 *
	 * @return connectedUser
	 */
	public AuthenticatedUser getAuthenticatedUser() {

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		AuthenticatedUser result = null;
		if (auth == null) {
			LOGGER.error("Null authentification");
		} else {
			final Object detail = auth.getDetails();
			if (detail == null) {
				LOGGER.error("User detail is null");
			} else {
				if (detail instanceof AuthenticatedUser authenticatedUser) {
					result = authenticatedUser;
				} else {
					LOGGER.error("Unknown authenticated user {}", auth.getPrincipal());
				}
			}

		}
		return result;
	}

	public boolean isRestricted() {
		AuthenticatedUser authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null) {
			return authenticatedUser.isRestricted();
		}
		return false;
	}

	public String getAuthenticatedUserLogin() {
		AuthenticatedUser authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null) {
			return authenticatedUser.getLogin();
		} else {
			return null;
		}
	}

	public boolean hasRole(String roleCode) {
		boolean result = false;
		AuthenticatedUser authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null && CollectionUtils.isNotEmpty(authenticatedUser.getRoles())) {
			result = authenticatedUser.getRoles().stream().anyMatch(r -> r.equalsIgnoreCase(roleCode));
		}
		return result;
	}

	public boolean hasAnyRole(List<String> roleCodes) {
		boolean result = false;
		AuthenticatedUser authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null && CollectionUtils.isNotEmpty(authenticatedUser.getRoles())) {
			List<String> lowerRoleCodes = roleCodes.stream().map(r -> r.toLowerCase()).toList();
			result = authenticatedUser.getRoles().stream().anyMatch(r -> lowerRoleCodes.contains(r.toLowerCase()));
		}
		return result;
	}

	public String getFeatureScope(String featureName) {
		AuthenticatedUser authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null) {
			return getAuthenticatedUser().getFeatureScope(featureName);
		} else {
			return null;
		}
	}

	public List<String> getOrganizations() {
		List<String> codes = new ArrayList<>();
		AuthenticatedUser authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null) {
			if (StringUtils.isNotEmpty(authenticatedUser.getMainOrganization())) {
				codes.add(authenticatedUser.getMainOrganization());
			}
			if (CollectionUtils.isNotEmpty(authenticatedUser.getOrganizations())) {
				for (String code : authenticatedUser.getOrganizations()) {
					if (StringUtils.isNotEmpty(code)) {
						codes.add(code);
					}

				}
			}
		}
		return codes;
	}

}
