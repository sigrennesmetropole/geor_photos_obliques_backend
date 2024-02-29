/**
 *
 */
package org.georchestra.photosobliques.service.exception;

/**
 * @author FNI18300
 *
 */
public class GeometryException extends AppServiceException {

	private static final long serialVersionUID = 4237843540707672236L;

	public GeometryException(String message) {
		super(message, AppServiceExceptionsStatus.INTERNAL_SERVER_ERROR);
	}

	public GeometryException(String message, Throwable cause) {
		super(message, cause, AppServiceExceptionsStatus.INTERNAL_SERVER_ERROR);
	}

}
