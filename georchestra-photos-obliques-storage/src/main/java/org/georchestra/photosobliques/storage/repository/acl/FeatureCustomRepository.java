package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.core.bean.acl.FeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.acl.FeatureEntity;

import java.util.List;

/**
 * Permet d'obtenir une liste de fonctionnalités
 */
public interface FeatureCustomRepository {

	List<FeatureEntity> searchFeatures(FeaturePhotosObliquesCriteria searchCriteria);
}
