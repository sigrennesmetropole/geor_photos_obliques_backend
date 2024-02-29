package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.storage.entity.acl.RoleFeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Dao pour les FeatureRole
 */
@Repository
public interface RoleFeatureRepository extends JpaRepository<RoleFeatureEntity,Long> {
    RoleFeatureEntity findByUuid(UUID uuid);
}
