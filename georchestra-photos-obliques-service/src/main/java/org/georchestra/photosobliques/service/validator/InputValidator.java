package org.georchestra.photosobliques.service.validator;

import org.georchestra.photosobliques.service.exception.AppServiceException;

/**
 * @param <T> DTO type
 */
public interface InputValidator<T> {

	void validateCreation(T dto) throws AppServiceException;

	void validateUpdate(T dto) throws AppServiceException;
}
