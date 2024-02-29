/**
 *
 */
package org.georchestra.photosobliques.service.exception;

/**
 * @author FNI18300
 *
 */
public class UsedByOtherException extends AppServiceException {

	private static final long serialVersionUID = 925256224388964853L;

	public UsedByOtherException(String message, Throwable cause) {
		super(message, cause, AppServiceExceptionsStatus.DELETE_FORBIDDEN);
	}

	public UsedByOtherException(String message) {
		super(message, AppServiceExceptionsStatus.DELETE_FORBIDDEN);
	}

}
