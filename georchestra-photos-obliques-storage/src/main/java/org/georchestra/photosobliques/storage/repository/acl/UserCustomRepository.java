package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.core.bean.acl.UserPhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.acl.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Permet d'obtenir une liste de users paginée et triée
 */
public interface UserCustomRepository {

	Page<UserEntity> searchUsers(UserPhotosObliquesCriteria searchCriteria, Pageable pageable);
}
