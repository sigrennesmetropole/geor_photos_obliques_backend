package org.georchestra.photosobliques.facade.controller.acl;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.core.bean.acl.FeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.facade.util.AbstractController;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.sm.acl.FeatureService;
import org.georchestra.photosobliques.facade.controller.api.FeaturesApi;
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
public class FeatureApiController extends AbstractController implements FeaturesApi {
    private final FeatureService featureService;

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')|| hasPermission('ADMINISTRATION_REFERENTIEL','READ_WRITE')")
    public ResponseEntity<Feature> createFeature(@Valid Feature feature) throws AppServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(featureService.createFeature(feature));
    }

    @Override
    public ResponseEntity<Feature> getFeature(UUID featureUuid) throws AppServiceException {
        return ResponseEntity.ok(featureService.getFeature(featureUuid));
    }

    @Override
    public ResponseEntity<List<Feature>> searchFeatures(@Valid Boolean active, @Valid String code, @Valid String label) {
        FeaturePhotosObliquesCriteria searchCriteria = FeaturePhotosObliquesCriteria.builder().active(active).code(code).label(label)
                .build();
        List<Feature> result = featureService.searchFeatures(searchCriteria);
        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')|| hasPermission('ADMINISTRATION_REFERENTIEL','READ_WRITE')")
    public ResponseEntity<Feature> updateFeature(@Valid Feature feature) throws AppServiceException {
        return ResponseEntity.ok(featureService.updateFeature(feature));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRATOR')|| hasPermission('ADMINISTRATION_REFERENTIEL','READ_WRITE_DELETE')")
    public ResponseEntity<Void> deleteFeature(UUID featureUuid) throws AppServiceException {
        featureService.deleteFeature(featureUuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
