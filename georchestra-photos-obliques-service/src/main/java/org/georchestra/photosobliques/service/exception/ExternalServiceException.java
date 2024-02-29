/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author FNI18300
 *
 */
public class ExternalServiceException extends AppServiceException {

	private static final long serialVersionUID = -7702884401953104705L;

	public ExternalServiceException(String serviceName, Throwable cause) {
		super("Erreur reçue du service externe " + serviceName, cause, AppServiceExceptionsStatus.BAD_GATEWAY);
	}
}
