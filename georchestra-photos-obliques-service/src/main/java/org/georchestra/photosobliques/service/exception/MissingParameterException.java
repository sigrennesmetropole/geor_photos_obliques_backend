/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.exception;

/**
 *
 * @author FNI18300
 *
 */
public class MissingParameterException extends AppServiceBadRequestException {

	private static final long serialVersionUID = -1803022995960998158L;

	public MissingParameterException(String message) {
		super(message);
	}
}
