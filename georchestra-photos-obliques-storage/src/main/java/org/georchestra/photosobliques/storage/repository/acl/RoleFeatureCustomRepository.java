package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.core.bean.acl.RoleFeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.acl.RoleFeatureEntity;

import java.util.List;

/**
 * Permet d'obtenir la liste des rolefeatures
 */
public interface RoleFeatureCustomRepository {
    List<RoleFeatureEntity> searchRolesFeature(RoleFeaturePhotosObliquesCriteria searchCriteria);
}
