/**
 *
 */
package org.georchestra.photosobliques.service.mapper.acl;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.georchestra.photosobliques.core.bean.UserCustomConfiguration;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.acl.UserCustomConfigurationEntity;

/**
 * @author FNI18300
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface UserCustomConfigurationMapper extends AbstractMapper<UserCustomConfigurationEntity, UserCustomConfiguration> {

	@Override
	@InheritInverseConfiguration
	UserCustomConfigurationEntity dtoToEntity(UserCustomConfiguration dto);

	/**
	 * Convertit un UserEntity en User.
	 *
	 * @param entity entity to transform to dto
	 * @return DossierDto
	 */
	@Override
	UserCustomConfiguration entityToDto(UserCustomConfigurationEntity entity);

	@Override
	void dtoToEntity(UserCustomConfiguration dto, @MappingTarget UserCustomConfigurationEntity entity);

}
