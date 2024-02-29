package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.storage.entity.acl.RoleEntity;
import org.georchestra.photosobliques.storage.repository.StampedRepository;
import org.springframework.stereotype.Repository;

/**
 * Dao pour les roles des utilisateurs
 * 
 * @author FNI18300
 *
 */
@Repository
public interface RoleRepository extends StampedRepository<RoleEntity> {
}
