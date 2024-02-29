package org.georchestra.photosobliques.service.sm.acl;

import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.core.bean.acl.FeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * @author NCA20245
 */
public interface FeatureService {
    List<Feature> searchFeatures(FeaturePhotosObliquesCriteria searchCriteria);

    Feature createFeature(Feature feature) throws AppServiceException;

    Feature updateFeature(Feature feature) throws AppServiceException;

    void deleteFeature(UUID uuid) throws AppServiceNotFoundException;

    Feature getFeature(UUID uuid) throws AppServiceNotFoundException;
}
