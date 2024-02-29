/**
 *
 */
package org.georchestra.photosobliques.service.exception;

/**
 * @author FNI18300
 *
 */
public class CsvImportException extends AppServiceException {

	private static final long serialVersionUID = 4237843540707672236L;

	public CsvImportException(String message) {
		super(message, AppServiceExceptionsStatus.ERROR_CSV);
	}

	public CsvImportException(String message, Throwable cause) {
		super(message, cause, AppServiceExceptionsStatus.ERROR_CSV);
	}

}
