package org.georchestra.photosobliques.service.validator;

import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;

public interface SearchCriteriaValidator<T> {

	void validateSearchCriteria(T searchCriteria) throws AppServiceBadRequestException;
}
