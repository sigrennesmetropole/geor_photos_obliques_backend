package org.georchestra.photosobliques.service.st.historic.impl;

import lombok.RequiredArgsConstructor;
import org.georchestra.photosobliques.core.bean.HistoricData;
import org.georchestra.photosobliques.core.bean.historic.HistoricDataPhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.service.mapper.historic.HistoricDataMapper;
import org.georchestra.photosobliques.service.st.historic.HistoricDataService;
import org.georchestra.photosobliques.storage.repository.historic.HistoricDataCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class HistoricDataServiceImpl implements HistoricDataService {

	private final HistoricDataCustomRepository repository;

	private final HistoricDataMapper historicDataMapper;

	private final Collection<HistoricDataSearchCriteriaValidator> validators;

	@Override
	public Page<HistoricData> searchHistoricDatum(HistoricDataPhotosObliquesCriteria searchCriteria, Pageable pageable) throws AppServiceBadRequestException {
		for (final HistoricDataSearchCriteriaValidator validator : validators) {
			validator.validateSearchCriteria(searchCriteria);
		}

		// TODO: Vérification des droits d'accès au which type demandé
		return historicDataMapper.entitiesToDto(repository.searchHistoricDatum(searchCriteria, pageable), pageable);
	}
}
