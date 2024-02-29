package org.georchestra.photosobliques.service.sm.acl.impl;

import org.georchestra.photosobliques.core.bean.RoleFeature;
import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.validator.AbstractValidator;
import org.georchestra.photosobliques.service.validator.InputValidator;
import org.springframework.stereotype.Component;

/**
 * @author NCA20245
 */
@Component
public class RoleFeatureValidator extends AbstractValidator<RoleFeature> implements InputValidator<RoleFeature> {
	@Override
	public void validateCreation(RoleFeature dto) throws AppServiceException {
		if (dto == null) {
			throw new AppServiceBadRequestException("validation.error.input.mandatory");
		}
		validateNotNullField(dto, "getScope", "validation.error.scope.mandatory");
	}

	@Override
	public void validateUpdate(RoleFeature dto) throws AppServiceException {
		if (dto == null) {
			throw new AppServiceBadRequestException("validation.error.input.mandatory");
		}
		validateNotNullField(dto, GET_UUID_METHOD, "validation.error.uuid.mandatory");
		validateCreation(dto);
	}
}
