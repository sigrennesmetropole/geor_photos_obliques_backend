/**
 *
 */
package org.georchestra.photosobliques.service.helper.authentification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author FNI18300
 *
 */
@Component(value = "authentificationHelper")
public class AuthentificationHelper {

	@Value("${georchestra.role.administrator}")
	private String georchestraAdministrator;

	/**
	 * @return l'username de la personne authentifiée
	 */
	public String getUsername() {
		String username = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() != null) {
			username = authentication.getPrincipal().toString();
		}
		return username;
	}

	/**
	 *
	 * @return vrai si l'utilisateur connecté est administrateur
	 */
	public boolean isAdmin() {
		return hasRole(georchestraAdministrator);
	}

	/**
	 * @return la liste des rôles
	 */
	public List<String> getRoles() {
		List<String> result = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			result = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.toList();
		}
		return result;
	}

	/**
	 *
	 * @param roleName
	 * @return
	 */
	public boolean hasRole(String roleName) {
		boolean result = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			result = authentication.getAuthorities().stream()
					.filter(authority -> authority.getAuthority().equalsIgnoreCase(roleName)).count() > 0;
		}
		return result;
	}
}
