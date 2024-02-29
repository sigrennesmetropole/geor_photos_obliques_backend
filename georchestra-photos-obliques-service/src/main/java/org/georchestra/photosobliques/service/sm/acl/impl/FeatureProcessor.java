package org.georchestra.photosobliques.service.sm.acl.impl;

import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.service.processor.AbstractStampedEntityProcessor;
import org.georchestra.photosobliques.storage.entity.acl.FeatureEntity;
import org.springframework.stereotype.Component;

/**
 * @author NCA20245
 */
@Component
public class FeatureProcessor extends AbstractStampedEntityProcessor<FeatureEntity, Feature> {
}
