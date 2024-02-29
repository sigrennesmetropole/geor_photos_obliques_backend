/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.aop;

import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 * @author FNI18300
 *
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

	private static final String DEFAULT_LOGIN = "anonymous";

	@Autowired
	private ACLHelper utilContextHelper;

	@Override
	public Optional<String> getCurrentAuditor() {
		String login = utilContextHelper.getAuthenticatedUserLogin();
		if (login == null) {
			login = DEFAULT_LOGIN;
		}
		return Optional.of(login);
	}

}
