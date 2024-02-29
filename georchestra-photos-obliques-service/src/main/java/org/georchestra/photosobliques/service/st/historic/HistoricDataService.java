package org.georchestra.photosobliques.service.st.historic;

import org.georchestra.photosobliques.core.bean.HistoricData;
import org.georchestra.photosobliques.core.bean.historic.HistoricDataPhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoricDataService {

	/**
	 * Récupération de l'historique de données
	 *
	 * @param searchCriteria Critères de recherche
	 * @param pageable       Pageable
	 * @return Les éléments d'historique correspondants aux critères de recherche, paginée selon le pageable
	 */
	Page<HistoricData> searchHistoricDatum(HistoricDataPhotosObliquesCriteria searchCriteria, Pageable pageable) throws AppServiceBadRequestException;
}
