/**
 *
 */
package org.georchestra.photosobliques.facade.configuration.security.common;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.Role;
import org.georchestra.photosobliques.core.bean.RoleFeature;
import org.georchestra.photosobliques.core.bean.acl.RoleFeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.core.bean.acl.RolePhotosObliquesCriteria;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.facade.configuration.security.cache.RoleFeatures;
import org.georchestra.photosobliques.facade.configuration.security.cache.RoleFeaturesManager;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.service.sm.acl.RoleFeatureService;
import org.georchestra.photosobliques.service.sm.acl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author FNI18300
 *
 */
@Component
@AllArgsConstructor
public class AuthenticationUserHelper {

	private static final String ROLE_PREFIX = "ROLE_";

	@Value("${spring.security.authenticatedUser.perimetre.claim:perimetre}")
	private String perimetreClaim;

	@Value("${spring.security.authenticatedUser.authority.restricted:search_restricted}")
	private String restrictedAuthority;

	private final RoleService roleService;

	private final RoleFeatureService roleFeatureService;

	private final RoleFeaturesManager roleFeaturesManager;

	protected final ACLHelper utilContextHelper;

	public AuthenticatedUser convert(OAuth2AuthenticationToken token, boolean full) {
		AuthenticatedUser authenticatedUser = initializeAuthenticatedUser(token);
		initializeRoles(authenticatedUser, (OidcUser) token.getPrincipal(), full);
		initializeOtherOrganizations(authenticatedUser, (OidcUser) token.getPrincipal());
		initializeRestricted(authenticatedUser, (OidcUser) token.getPrincipal());

		return authenticatedUser;
	}

	protected void initializeRestricted(AuthenticatedUser authenticatedUser, OidcUser user) {
		if (CollectionUtils.isNotEmpty(user.getAuthorities())) {
			for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
				if (removeRolePrefix(grantedAuthority.getAuthority()).equalsIgnoreCase(restrictedAuthority)) {
					authenticatedUser.setRestricted(true);
					break;
				}
			}
		}
	}

	protected void initializeOtherOrganizations(AuthenticatedUser authenticatedUser, OidcUser principal) {
		String perimetre = principal.getClaim(perimetreClaim);
		if (StringUtils.isNotEmpty(perimetre)) {
			String[] perimetreParts = perimetre.split(" ");
			for (String perimetrePart : perimetreParts) {
				if (StringUtils.isNotEmpty(perimetrePart)) {
					authenticatedUser.addOrganization(perimetrePart.trim().toUpperCase());
				}
			}
		}
	}

	/**
	 * Il est prévu de ne conserver que les droits du rôle le plus important
	 *
	 * @param connectedUser the authenticatedUser
	 * @param user          the user
	 * @parem full pour avoir tout ou partie des features
	 */
	protected void initializeRoles(AuthenticatedUser authenticatedUser, OidcUser user, boolean full) {
		if (CollectionUtils.isNotEmpty(user.getAuthorities())) {
			List<Role> roles = roleService.searchRoles(RolePhotosObliquesCriteria.builder().active(true).build());
			for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
				// on cherche le role associé au profil
				Role role = lookupRoleByLdapCode(roles, grantedAuthority.getAuthority());
				// si le role existe et qu'on a pas encore de role ou que le role a un "order"
				// supérieur (donc moins prioritaire)
				if (role != null) {
					authenticatedUser.addRole(role.getCode());
					if (full) {
						initializeRoleFeature(authenticatedUser, role);
					}
				}
			}
		}
	}

	public void initializeRoleFeature(AuthenticatedUser authenticatedUser) {
		if (authenticatedUser != null && CollectionUtils.isNotEmpty(authenticatedUser.getRoles())) {
			List<Role> roles = new ArrayList<>();
			for (String roleCode : authenticatedUser.getRoles()) {

				// on regarde si les données de featues sont en cache pour le role
				RoleFeatures roleFeatures = roleFeaturesManager.lookupRoleFeatures(roleCode);
				if (roleFeatures == null) {
					roleFeatures = storeRoleFeatures(roleCode, roles);
				}

				// on assigne les features dans le connected user
				authenticatedUser.putFeatures(roleFeatures.getFeatureScopeValues());
			}
		}
	}

	private RoleFeatures storeRoleFeatures(String roleCode, @NotNull List<Role> roles) {
		if (roles.isEmpty()) {
			roles.addAll(roleService.searchRoles(RolePhotosObliquesCriteria.builder().active(true).build()));
		}
		Role role = lookupRoleByCode(roles, roleCode);
		// si c'est pas le cas on le met en cache
		RoleFeatures roleFeatures = new RoleFeatures(role);
		List<RoleFeature> roleFeaturesForRole = roleFeatureService
				.searchRolesFeature(RoleFeaturePhotosObliquesCriteria.builder().roleUuid(role.getUuid()).build());
		if (CollectionUtils.isNotEmpty(roleFeaturesForRole)) {
			for (RoleFeature roleFeature : roleFeaturesForRole) {
				roleFeatures.putFeature(roleFeature.getFeature(), roleFeature.getScope());
			}
		}
		roleFeaturesManager.storeRoleFeatures(roleFeatures);
		return roleFeatures;
	}

	protected void initializeRoleFeature(AuthenticatedUser authenticatedUser, Role role) {
		if (role != null) {
			// on regarde si les données de featues sont en cache pour le role
			RoleFeatures roleFeatures = roleFeaturesManager.lookupRoleFeatures(role.getCode());
			if (roleFeatures == null) {
				// si c'est pas le cas on le met en cache
				roleFeatures = new RoleFeatures(role);
				List<RoleFeature> roleFeaturesForRole = roleFeatureService
						.searchRolesFeature(RoleFeaturePhotosObliquesCriteria.builder().roleUuid(role.getUuid()).build());
				if (CollectionUtils.isNotEmpty(roleFeaturesForRole)) {
					for (RoleFeature roleFeature : roleFeaturesForRole) {
						roleFeatures.putFeature(roleFeature.getFeature(), roleFeature.getScope());
					}
				}
				roleFeaturesManager.storeRoleFeatures(roleFeatures);
			}

			// on assigne les features dans le connected user
			authenticatedUser.putFeatures(roleFeatures.getFeatureScopeValues());
		}
	}

	protected Collection<GrantedAuthority> collectAuthorities(AuthenticatedUser authenticatedUser) {
		List<GrantedAuthority> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(authenticatedUser.getRoles())) {
			for (String roleCode : authenticatedUser.getRoles()) {
				result.add(new SimpleGrantedAuthority(roleCode));
			}
		}
		return result;
	}

	protected Role lookupRoleByLdapCode(List<Role> roles, String code) {
		code = removeRolePrefix(code);
		Role result = null;
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				if (role.getCodeLdap().equalsIgnoreCase(code)) {
					result = role;
					break;
				}
			}
		}
		return result;
	}

	protected Role lookupRoleByCode(List<Role> roles, String code) {
		code = removeRolePrefix(code);
		Role result = null;
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				if (role.getCode().equalsIgnoreCase(code)) {
					result = role;
					break;
				}
			}
		}
		return result;
	}

	protected AuthenticatedUser initializeAuthenticatedUser(OAuth2AuthenticationToken token) {
		OidcUser user = (OidcUser) token.getPrincipal();
		AuthenticatedUser connectedUser = new AuthenticatedUser();
		connectedUser.setLogin(user.getSubject());
		connectedUser.setEmail(user.getEmail());
		connectedUser.setFirstname(user.getGivenName());
		connectedUser.setLastname(user.getFamilyName());
		return connectedUser;
	}

	protected String removeRolePrefix(String code) {
		if (code.startsWith(ROLE_PREFIX)) {
			return code.substring(ROLE_PREFIX.length());
		}
		return code;
	}
}
