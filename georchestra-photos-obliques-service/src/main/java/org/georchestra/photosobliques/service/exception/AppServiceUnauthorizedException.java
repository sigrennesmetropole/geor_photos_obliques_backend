/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author FNI18300
 *
 */
public class AppServiceUnauthorizedException extends AppServiceException {

	private static final long serialVersionUID = -7422681521262782716L;

	public AppServiceUnauthorizedException(String message) {
		super(message, AppServiceExceptionsStatus.UNAUTHORIZE);
	}
}
