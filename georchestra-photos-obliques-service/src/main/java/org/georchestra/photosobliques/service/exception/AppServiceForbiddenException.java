/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author FNI18300
 *
 */
public class AppServiceForbiddenException extends AppServiceException {

	private static final long serialVersionUID = 3681228672910674256L;

	public AppServiceForbiddenException(String message) {
		super(message, AppServiceExceptionsStatus.FORBIDDEN);
	}
}
