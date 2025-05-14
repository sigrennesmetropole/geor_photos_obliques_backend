package org.georchestra.photosobliques.service.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.service.sm.configuration.ConfigurationService;
import org.georchestra.photosobliques.storage.phototheque.entity.PhotoObliqueEntity;
import org.locationtech.jts.geom.Geometry;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

/**
 * @author NCA20245
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface PhotoObliqueMapper {

    List<PhotoOblique> entityToDto(List<PhotoObliqueEntity> entity, @Context ConfigurationService configurationService);
    /**
     * Convertit un FeatureEntity en Feature.
     *
     * @param entity entity to transform to dto
     * @return DossierDto
     */
    @Mapping(source = "shape", target = "shape", qualifiedByName = "shapeToString")
    @Mapping(source = "date", target = "date", qualifiedByName = "dateToDateString")
    PhotoOblique entityToDto(PhotoObliqueEntity entity,  @Context ConfigurationService configurationService);


    @AfterMapping
    default void afterMapping(PhotoObliqueEntity source, @MappingTarget PhotoOblique target, @Context ConfigurationService configurationService) {
        target.urlOverview(configurationService.getApplicationConfiguration().getUrlOverview() + source.getFile());
        target.urlThumbnail(configurationService.getApplicationConfiguration().getUrlThumbnail() + source.getFile());
    }

    @Named("shapeToString")
    static String shapeToString(Geometry shape) {
        return shape.toString();
    }

    @Named("dateToDateString")
    static String dateToDateString(Date date) {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        return formatDate.format(date);
    }
}
