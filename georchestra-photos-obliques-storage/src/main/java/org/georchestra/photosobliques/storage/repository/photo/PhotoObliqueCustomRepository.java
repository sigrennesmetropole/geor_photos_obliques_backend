package org.georchestra.photosobliques.storage.repository.photo;

import jakarta.persistence.Tuple;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Permet d'obtenir la liste des rolefeatures
 */
public interface PhotoObliqueCustomRepository {

    List<Tuple> searchPhotosObliquesWithRelevance(PhotoObliqueSearchCriteria searchCriteria, Double tolerence, Pageable pageable);
    List<String> searchOwners(String geometry);
    List<String> searchProviders(String geometry);
    List<Integer> searchYears(String geometry);

    Integer countPhotosObliques(PhotoObliqueSearchCriteria searchCriteria, Double tolerence);
}
