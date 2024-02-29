package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author fni18300
 *
 */
public class AsyncInformationException extends AppServiceException {

	private static final long serialVersionUID = 5747244696042729320L;

	public AsyncInformationException(String message) {
		super(message, AppServiceExceptionsStatus.BAD_REQUEST);
	}

}
