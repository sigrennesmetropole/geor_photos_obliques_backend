package org.georchestra.photosobliques.facade.controller.acl;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.FeatureScope;
import org.georchestra.photosobliques.core.bean.RoleFeature;
import org.georchestra.photosobliques.core.bean.acl.RoleFeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.facade.util.AbstractController;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.sm.acl.RoleFeatureService;
import org.georchestra.photosobliques.facade.controller.api.RolesFeatureApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author NCA20245
 *
 */
@RestController
@AllArgsConstructor
public class RoleFeatureApiController extends AbstractController implements RolesFeatureApi {

    private final RoleFeatureService featureService;

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')|| hasRoleFeature('Administration','READ_WRITE')")
    public ResponseEntity<RoleFeature> createRoleFeature(@Valid RoleFeature roleFeature) throws AppServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createRoleFeature(roleFeature));
    }

    @Override
    public ResponseEntity<RoleFeature> getRoleFeature(UUID roleFeatureUuid) throws AppServiceException {
        return ResponseEntity.ok(featureService.getRoleFeature(roleFeatureUuid));
    }

    @Override
    public ResponseEntity<List<RoleFeature>> searchRolesFeature(@Valid String scope) {
        RoleFeaturePhotosObliquesCriteria searchCriteria = RoleFeaturePhotosObliquesCriteria.builder().scope(FeatureScope.valueOf(scope))
                .build();
        List<RoleFeature> result = featureService.searchRolesFeature(searchCriteria);
        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')|| hasRoleFeature('Administration','READ_WRITE')")
    public ResponseEntity<RoleFeature> updateRoleFeature(@Valid RoleFeature roleFeature) throws AppServiceException {
        return ResponseEntity.ok(featureService.updateRoleFeature(roleFeature));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')|| hasRoleFeature('Administration','READ_WRITE_DELETE')")
    public ResponseEntity<Void> deleteRoleFeature(UUID roleFeatureUuid) throws AppServiceException {
        featureService.deleteRoleFeature(roleFeatureUuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
