package org.georchestra.photosobliques.service.sm.acl.impl;

import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.sm.validator.AbstractStampedInputValidator;
import org.springframework.stereotype.Component;

/**
 * @author NCA20245
 *
 */
@Component
public class FeatureValidator extends AbstractStampedInputValidator<Feature> {

	@Override
	public void validateCreation(Feature dto) throws AppServiceException {
		super.validateCreation(dto);
		validateNotNullField(dto, "getAvailablescope", "validation.error.availablescope.mandatory");
	}
}
