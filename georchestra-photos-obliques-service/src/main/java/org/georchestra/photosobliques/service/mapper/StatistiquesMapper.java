package org.georchestra.photosobliques.service.mapper;

import org.georchestra.photosobliques.core.bean.statistiques.Statistiques;
import org.georchestra.photosobliques.storage.statistiques.entity.StatistiquesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface StatistiquesMapper {

    /**
     * Convertit un FeatureEntity en Feature.
     *
     * @param entity entity to transform to dto
     * @return DossierDto
     */
    Statistiques entityToDto(StatistiquesEntity entity);

    /**
     * Convertit un FeatureEntity en Feature.
     *
     * @param dto dto to transform to entity
     * @return DossierDto
     */
    StatistiquesEntity dtoToEntity(Statistiques dto);

}
