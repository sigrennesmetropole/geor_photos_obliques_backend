package org.georchestra.photosobliques.service.helper.acl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.User;
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
	public User getAuthenticatedUser() {

		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User result = null;
		if (auth == null) {
			LOGGER.error("Null authentification");
		} else {
			final Object detail = auth.getDetails();
			if (detail == null) {
				LOGGER.error("User detail is null");
			} else {
				if (detail instanceof User authenticatedUser) {
					result = authenticatedUser;
				} else {
					LOGGER.error("Unknown authenticated user {}", auth.getPrincipal());
				}
			}

		}
		return result;
	}

	public String getAuthenticatedUserLogin() {
		User authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null) {
			return authenticatedUser.getLogin();
		} else {
			return null;
		}
	}

	public boolean hasRole(String roleCode) {
		boolean result = false;
		User authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null && CollectionUtils.isNotEmpty(authenticatedUser.getRoles())) {
			result = authenticatedUser.getRoles().stream().anyMatch(r -> r.equalsIgnoreCase(roleCode));
		}
		return result;
	}

	public boolean hasAnyRole(List<String> roleCodes) {
		boolean result = false;
		User authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null && CollectionUtils.isNotEmpty(authenticatedUser.getRoles())) {
			List<String> lowerRoleCodes = roleCodes.stream().map(String::toLowerCase).toList();
			result = authenticatedUser.getRoles().stream().anyMatch(r -> lowerRoleCodes.contains(r.toLowerCase()));
		}
		return result;
	}

	public List<String> getOrganizations() {
		List<String> codes = new ArrayList<>();
		User authenticatedUser = getAuthenticatedUser();
		if (authenticatedUser != null && StringUtils.isNotEmpty(authenticatedUser.getOrganization())) {
				codes.add(authenticatedUser.getOrganization());
		}
		return codes;
	}

}
