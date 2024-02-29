/**
 *
 */
package org.georchestra.photosobliques.service.exception;

/**
 * @author FNI18300
 *
 */
public class CsvGenerationException extends AppServiceException {

	private static final long serialVersionUID = 4237843540707672236L;

	public CsvGenerationException(String message) {
		super(message, AppServiceExceptionsStatus.ERROR_CSV);
	}

	public CsvGenerationException(String message, Throwable cause) {
		super(message, cause, AppServiceExceptionsStatus.ERROR_CSV);
	}

}
