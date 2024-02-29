package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author FNI18300
 *
 */
public class UnzipException extends AppServiceException {

	private static final long serialVersionUID = 1397189539887382061L;

	public UnzipException() {
		super(AppServiceExceptionsStatus.ERROR_UNZIP_FILE.getStringValue(),
				AppServiceExceptionsStatus.ERROR_UNZIP_FILE);
	}

	public UnzipException(String message, Throwable cause) {
		super(message, cause, AppServiceExceptionsStatus.ERROR_UNZIP_FILE);
	}

	public UnzipException(String message) {
		super(message, AppServiceExceptionsStatus.ERROR_UNZIP_FILE);
	}

}
