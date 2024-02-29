package org.georchestra.photosobliques.service.mapper.historic;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.georchestra.photosobliques.core.bean.HistoricData;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class})
public interface HistoricDataMapper extends AbstractMapper<HistoricDataEntity, HistoricData> {
}
