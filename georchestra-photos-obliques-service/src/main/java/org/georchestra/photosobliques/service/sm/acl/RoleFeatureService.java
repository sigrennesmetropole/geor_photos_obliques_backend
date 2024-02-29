package org.georchestra.photosobliques.service.sm.acl;

import org.georchestra.photosobliques.core.bean.RoleFeature;
import org.georchestra.photosobliques.core.bean.acl.RoleFeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * @author NCA20245
 */
public interface RoleFeatureService {
    List<RoleFeature> searchRolesFeature(RoleFeaturePhotosObliquesCriteria searchCriteria);

    RoleFeature createRoleFeature(RoleFeature feature) throws AppServiceException;

    RoleFeature updateRoleFeature(RoleFeature feature) throws AppServiceException;

    void deleteRoleFeature(UUID uuid) throws AppServiceNotFoundException;

    RoleFeature getRoleFeature(UUID uuid) throws AppServiceNotFoundException;
}
