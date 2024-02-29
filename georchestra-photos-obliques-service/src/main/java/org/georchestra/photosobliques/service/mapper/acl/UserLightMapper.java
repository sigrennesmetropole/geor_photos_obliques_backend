/**
 *
 */
package org.georchestra.photosobliques.service.mapper.acl;

import org.mapstruct.*;
import org.georchestra.photosobliques.core.bean.User;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.acl.UserEntity;

/**
 * @author FNI18300
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface UserLightMapper extends AbstractMapper<UserEntity, User> {

	@Override
	@InheritInverseConfiguration
	UserEntity dtoToEntity(User dto);

	/**
	 * Convertit un UserEntity en User.
	 *
	 * @param entity entity to transform to dto
	 * @return DossierDto
	 */
	@Override
	User entityToDto(UserEntity entity);

	@Override
	@Mapping(target = "login", source = "login", ignore = true)
	@Mapping(target = "type", source = "type", ignore = true)
	void dtoToEntity(User dto, @MappingTarget UserEntity entity);

}
