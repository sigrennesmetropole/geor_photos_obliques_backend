package org.georchestra.photosobliques.storage.repository.historic;

import org.georchestra.photosobliques.core.bean.historic.HistoricDataPhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoricDataCustomRepository {
    Page<HistoricDataEntity> searchHistoricDatum(HistoricDataPhotosObliquesCriteria searchCriteria, Pageable pageable);
}
