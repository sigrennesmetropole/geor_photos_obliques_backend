/**
 *
 */
package org.georchestra.photosobliques.service.mapper.async;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.georchestra.photosobliques.core.bean.AsyncInformation;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.async.AsyncInformationEntity;

/**
 * @author FNI18300
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class})
public interface AsyncInformationMapper extends AbstractMapper<AsyncInformationEntity, AsyncInformation> {

}
