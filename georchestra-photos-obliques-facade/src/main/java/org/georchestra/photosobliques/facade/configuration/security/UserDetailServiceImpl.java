/**
 * SHOM Search
 */
package org.georchestra.photosobliques.facade.configuration.security;

import org.georchestra.photosobliques.core.bean.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author FNI18300
 *
 */
public class UserDetailServiceImpl extends AbstractDetailServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = getUserService().getUserByLogin(login);
		if (user == null) {
			throw new UsernameNotFoundException("Unknown login:" + login);
		}
		List<GrantedAuthority> grantedAuthorities = computeGrantedAuthorities(user);
		return new org.springframework.security.core.userdetails.User(login, "", grantedAuthorities);
	}

}
