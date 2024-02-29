/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.sm.validator;

import org.georchestra.photosobliques.service.exception.AppServiceException;

/**
 * @author FNI18300
 */
public abstract class AbstractStampedInputValidator<T> extends AbstractLongIdInputValidator<T> {

	@Override
	public void validateCreation(T dto) throws AppServiceException {
		super.validateCreation(dto);
		validateStampedCommon(dto);
	}

	@Override
	public void validateUpdate(T dto) throws AppServiceException {
		super.validateUpdate(dto);
		validateStampedCommon(dto);
	}

	private void validateStampedCommon(T dto) throws AppServiceException {
		validateStringField(dto, "getCode", "validation.error.code.mandatory");
		validateStringField(dto, "getLabel", "validation.error.label.mandatory");
		validateNotNullField(dto, "getOpeningDate", "validation.error.openingdate.mandatory");
		validateRangeDateField(dto, "getOpeningDate", "getClosingDate",
						"validation.error.closingdate.beforeopeningdate");
	}

}
