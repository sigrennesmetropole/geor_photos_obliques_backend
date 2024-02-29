package org.georchestra.photosobliques.service.mapper.acl;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.acl.FeatureEntity;

/**
 * @author NCA20245
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface FeatureMapper extends AbstractMapper<FeatureEntity, Feature> {
    @Override
    @InheritInverseConfiguration
    FeatureEntity dtoToEntity(Feature dto);

    /**
     * Convertit un FeatureEntity en Feature.
     *
     * @param entity entity to transform to dto
     * @return DossierDto
     */
    @Override
    Feature entityToDto(FeatureEntity entity);

    @Override
    void dtoToEntity(Feature dto, @MappingTarget FeatureEntity entity);
}
