/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.sm.validator;

import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.validator.AbstractValidator;
import org.georchestra.photosobliques.service.validator.InputValidator;

/**
 * @author FNI18300
 */
public abstract class AbstractLongIdInputValidator<T> extends AbstractValidator<T> implements InputValidator<T> {

	@Override
	public void validateCreation(T dto) throws AppServiceException {
		validateCommonLongId(dto);
	}

	@Override
	public void validateUpdate(T dto) throws AppServiceException {
		validateCommonLongId(dto);
		validateNotNullField(dto, "getUuid", "validation.error.uuid.mandatory");
	}

	private void validateCommonLongId(T dto) throws AppServiceBadRequestException {
		if (dto == null) {
			throw new AppServiceBadRequestException("validation.error.input.mandatory");
		}
	}
}
