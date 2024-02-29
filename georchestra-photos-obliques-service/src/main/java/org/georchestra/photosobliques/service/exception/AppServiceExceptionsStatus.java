package org.georchestra.photosobliques.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Codes des status remontés en front en cas d'erreur de saisie de l'utilsateur.
 */
@Getter
public enum AppServiceExceptionsStatus {

	BAD_REQUEST("ERROR_FORBIDDEN_400", HttpStatus.BAD_REQUEST),
	UNAUTHORIZE("ERROR_UNAUTHORIZE_401", HttpStatus.UNAUTHORIZED), FORBIDDEN(HttpStatus.FORBIDDEN),
	NOT_FOUND(HttpStatus.NOT_FOUND), BUSINESS_ERROR("409_BUSINESS_ERROR", 409),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR), UNKNOWN_CLIENT_KEY("ERROR_UNKNOWN_CLIENT_KEY_480", 480),
	BAD_GATEWAY(HttpStatus.BAD_GATEWAY), DELETE_FORBIDDEN("500_DELETE_FORBIDDEN", 546),
	ERROR_UNZIP_FILE("500_ERROR_UNZIP_FILE", 547), ERROR_CSV("500_ERROR_CSV", 548),
	ERROR_FILE_TOO_LARGE("413_ERROR_FILE_TOO_LARGE", HttpStatus.PAYLOAD_TOO_LARGE);

	private final String stringValue;
	private final HttpStatus httpStatus;
	private final int customHttpStatusCode;

	AppServiceExceptionsStatus(String stringValue, HttpStatus httpStatus) {
		this.stringValue = stringValue;
		this.httpStatus = httpStatus;
		customHttpStatusCode = httpStatus.value();
	}

	AppServiceExceptionsStatus(String stringValue, int customHttpStatusCode) {
		this.stringValue = stringValue;
		this.httpStatus = null;
		this.customHttpStatusCode = customHttpStatusCode;
	}

	AppServiceExceptionsStatus(HttpStatus httpStatus) {
		this(stringValueFrom(httpStatus), httpStatus);
	}

	protected static String stringValueFrom(HttpStatus httpStatus) {
		return "ERROR_" + httpStatus.name() + "_" + httpStatus.value();
	}

}
