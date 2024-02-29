package org.georchestra.photosobliques.service.st.historic.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.georchestra.photosobliques.core.bean.historic.HistoricDataPhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.service.validator.AbstractValidator;
import org.georchestra.photosobliques.service.validator.SearchCriteriaValidator;
import org.springframework.stereotype.Component;

@Component
public class HistoricDataSearchCriteriaValidator extends AbstractValidator<HistoricDataPhotosObliquesCriteria> implements SearchCriteriaValidator<HistoricDataPhotosObliquesCriteria> {

	private static final String[] allowedWhichType = new String[]{
					"org.georchestra.photosobliques.storage.entity.personne.PersonnePhysiqueEntity",
					"org.georchestra.photosobliques.storage.entity.personne.PersonneMoraleEntity",
					"org.georchestra.photosobliques.storage.entity.foret.ProprieteForestiereEntity",
					"org.georchestra.photosobliques.storage.entity.dossier.DossierEntity",
					"org.georchestra.photosobliques.storage.entity.programmeProgrammeEntity"
	};

	@Override
	public void validateSearchCriteria(HistoricDataPhotosObliquesCriteria searchCriteria) throws AppServiceBadRequestException {
		if (searchCriteria == null) {
			throw new AppServiceBadRequestException("validation.error.input.mandatory");
		}
		validateNotNullField(searchCriteria, "getWhichUuid", "validation.error.uuidWhich.mandatory");
		validateNotNullField(searchCriteria, "getWhichType", "validation.error.whichType.notAllowed");

		if (!ArrayUtils.contains(allowedWhichType,searchCriteria.getWhichType())) {
			throw new AppServiceBadRequestException("validation.error.whichType.notAllowed");
		}

		if (searchCriteria.getDateMin() != null && searchCriteria.getDateMax() != null) {
			validateRangeDateField(searchCriteria, "getDateMin", "getDateMax", "validation.error.dates.invalidOrder");
		}

	}
}
