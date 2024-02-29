package org.georchestra.photosobliques.service.mapper.acl;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.georchestra.photosobliques.core.bean.RoleFeature;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.acl.RoleFeatureEntity;

/**
 * @author NCA20245
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface RoleFeatureMapper extends AbstractMapper<RoleFeatureEntity, RoleFeature> {
    @Override
    @InheritInverseConfiguration
    RoleFeatureEntity dtoToEntity(RoleFeature dto);

    /**
     * Convertit un RoleFeatureEntity en RoleFeature.
     *
     * @param entity entity to transform to dto
     * @return DossierDto
     */
    @Override
    RoleFeature entityToDto(RoleFeatureEntity entity);

    @Override
    void dtoToEntity(RoleFeature dto, @MappingTarget RoleFeatureEntity entity);
}
