package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.storage.entity.acl.FeatureEntity;
import org.georchestra.photosobliques.storage.repository.StampedRepository;
import org.springframework.stereotype.Repository;

/**
 * Dao pour les fonctionnalités
 * 
 * @author FNI18300
 *
 */
@Repository
public interface FeatureRepository extends StampedRepository<FeatureEntity> {
}
