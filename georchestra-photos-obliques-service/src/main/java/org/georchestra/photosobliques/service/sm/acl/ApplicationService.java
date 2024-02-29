/**
 *
 */
package org.georchestra.photosobliques.service.sm.acl;

/**
 * @author FNI18300
 *
 */
public interface ApplicationService {

	/**
	 *
	 * @return l'URL du front
	 */
	String getFrontURL();

	/**
	 * @param token
	 * @return l'url du front avec le token
	 */
	String computeFrontURL(String token);
}
