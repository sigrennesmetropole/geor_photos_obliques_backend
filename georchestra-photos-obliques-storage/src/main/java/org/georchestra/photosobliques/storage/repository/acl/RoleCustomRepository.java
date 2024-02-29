package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.core.bean.acl.RolePhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.acl.RoleEntity;

import java.util.List;

/**
 * Permet d'obtenir une liste de rôles pour utilisateurs
 */
public interface RoleCustomRepository {

	List<RoleEntity> searchRoles(RolePhotosObliquesCriteria searchCriteria);
}
