/**
 * SHOM Search
 */
package org.georchestra.photosobliques.facade.configuration.security;

import org.apache.commons.collections4.CollectionUtils;
import org.georchestra.photosobliques.core.bean.Role;
import org.georchestra.photosobliques.core.bean.User;
import org.georchestra.photosobliques.service.sm.acl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FNI18300
 *
 */
public class AbstractDetailServiceImpl {

	@Autowired
	private UserService userService;

	protected List<GrantedAuthority> computeGrantedAuthorities(User user) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(user.getRoles())) {
			for (String role : user.getRoles()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			}
		}
		return grantedAuthorities;
	}

	protected List<String> computeRoles(User user) {
		return user.getRoles();
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService() {
		return userService;
	}
}
