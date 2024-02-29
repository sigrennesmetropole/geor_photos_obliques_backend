/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author FNI18300
 *
 */
public class AppServiceBadRequestException extends AppServiceException {

	private static final long serialVersionUID = 2355086279113118198L;

	public AppServiceBadRequestException(String message) {
		super(message, AppServiceExceptionsStatus.BAD_REQUEST);
	}
}
