/**
 *
 */
package org.georchestra.photosobliques.service.exception;

import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * @author FNI18300
 *
 */
public class GeoserverException extends ExternalServiceException {

	private static final long serialVersionUID = 392778257372005935L;

	public GeoserverException(String serviceName, Throwable cause) {
		super(serviceName, cause);
	}

	public GeoserverException(WebClientResponseException cause) {
		this("Geoserver", cause);
	}

}
