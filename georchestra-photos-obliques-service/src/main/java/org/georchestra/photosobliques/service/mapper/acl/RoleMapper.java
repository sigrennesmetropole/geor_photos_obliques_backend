/**
 *
 */
package org.georchestra.photosobliques.service.mapper.acl;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.georchestra.photosobliques.core.bean.Role;
import org.georchestra.photosobliques.service.mapper.AbstractMapper;
import org.georchestra.photosobliques.service.mapper.MapperUtils;
import org.georchestra.photosobliques.storage.entity.acl.RoleEntity;

/**
 * @author MCY12700
 *
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MapperUtils.class })
public interface RoleMapper extends AbstractMapper<RoleEntity, Role> {

	@Override
	@InheritInverseConfiguration
	RoleEntity dtoToEntity(Role dto);

	/**
	 * Converti un RoleEntity en Role.
	 *
	 * @param entity entity to transform to dto
	 * @return Role
	 */
	@Override
	Role entityToDto(RoleEntity entity);

	@Override
	void dtoToEntity(Role dto, @MappingTarget RoleEntity entity);

}
