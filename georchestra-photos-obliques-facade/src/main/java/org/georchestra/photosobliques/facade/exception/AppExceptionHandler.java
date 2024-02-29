package org.georchestra.photosobliques.facade.exception;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.Data;
import lombok.val;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceExceptionsStatus;
import org.georchestra.photosobliques.service.exception.AppServiceNotFoundException;
import org.georchestra.photosobliques.service.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ValidationException;

@ControllerAdvice
public class AppExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);

	@Data
	private static class ApiError {
		private final String code;
		private final String label;
	}

	@ExceptionHandler(org.georchestra.photosobliques.service.exception.AppServiceException.class)
	protected ResponseEntity<Object> handleExceptionService(final AppServiceException ex) {

		LOGGER.error(ex.getMessage(), ex);

		final AppServiceExceptionsStatus appExceptionStatusCode = ex.getAppExceptionStatusCode();
		if (appExceptionStatusCode != null) {

			val httpStatus = appExceptionStatusCode.getHttpStatus();
			if (httpStatus != null) {
				if (httpStatus.is4xxClientError()) {
					return ResponseEntity.status(httpStatus).body(buildBody(ex, httpStatus));
				} else {
					return ResponseEntity.status(httpStatus).build();
				}
			}

			final int customHttpStatusCode = appExceptionStatusCode.getCustomHttpStatusCode();
			if (customHttpStatusCode > 0) {
				return ResponseEntity.status(customHttpStatusCode).body(buildBody(ex, customHttpStatusCode));
			}
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@ExceptionHandler({ AccessDeniedException.class, AuthenticationCredentialsNotFoundException.class,
			LockedException.class, BadCredentialsException.class })
	protected ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler({ AppServiceNotFoundException.class })
	protected ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
		LOGGER.error("Ressource not found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ExceptionHandler({ IllegalAccessException.class })
	protected ResponseEntity<Object> handleIllegalAccessException(final Exception ex, final WebRequest request) {
		LOGGER.error("Ressource not authorized");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler({ ValidationException.class, JsonParseException.class, HttpMessageNotReadableException.class,
			MethodArgumentNotValidException.class, MissingServletRequestParameterException.class })
	protected ResponseEntity<Object> handleValidationException(final Exception ex, final WebRequest request) {
		LOGGER.error("Ressource not valid");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildBody(ex, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMapUploadSizeException(final Exception ex, final WebRequest request) {
		return ResponseEntity.status(AppServiceExceptionsStatus.ERROR_FILE_TOO_LARGE.getCustomHttpStatusCode())
				.body(buildBody("Le fichier téléversé est trop grand",
						AppServiceExceptionsStatus.ERROR_FILE_TOO_LARGE.getCustomHttpStatusCode()));
	}
	
	@ExceptionHandler({BusinessException.class})
	protected ResponseEntity<Object> handleBusinessException(final Exception ex, final WebRequest request) {
		return ResponseEntity.status(409)
				.body(buildBody(ex, AppServiceExceptionsStatus.BUSINESS_ERROR.getCustomHttpStatusCode()));
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(final Exception ex, final WebRequest request) {
		LOGGER.error("Unknown error", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	protected ApiError buildBody(Exception ex, HttpStatus status) {
		return buildBody(ex.getLocalizedMessage(), status);
	}

	protected ApiError buildBody(String message, HttpStatus status) {
		return new ApiError(status.toString(), message);
	}

	protected ApiError buildBody(Exception ex, int customHttpStatusCode) {
		return buildBody(ex.getLocalizedMessage(), customHttpStatusCode);
	}

	protected ApiError buildBody(String message, int customHttpStatusCode) {
		return new ApiError("HTTP_" + customHttpStatusCode, message);
	}
}
