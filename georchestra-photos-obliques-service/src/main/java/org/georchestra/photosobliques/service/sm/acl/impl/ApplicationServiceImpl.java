/**
 *
 */
package org.georchestra.photosobliques.service.sm.acl.impl;

import org.georchestra.photosobliques.service.sm.acl.ApplicationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author FNI18300
 *
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {

	@Value("${photos_obliques.front.url:''}")
	private String frontURL;

	@Override
	public String getFrontURL() {
		return frontURL;
	}

	@Override
	public String computeFrontURL(String token) {
		String prefix = getFrontURL();
		StringBuilder url = new StringBuilder(prefix);
		if (prefix.contains("?")) {
			url.append("&token=").append(token);
		} else {
			url.append("?token=").append(token);
		}
		return url.toString();
	}

}
