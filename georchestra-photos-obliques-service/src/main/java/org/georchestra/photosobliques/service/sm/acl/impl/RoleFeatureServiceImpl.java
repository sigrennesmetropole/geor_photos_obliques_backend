/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.sm.acl.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.georchestra.photosobliques.core.bean.RoleFeature;
import org.georchestra.photosobliques.core.bean.acl.RoleFeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceNotFoundException;
import org.georchestra.photosobliques.service.mapper.acl.RoleFeatureMapper;
import org.georchestra.photosobliques.service.sm.acl.RoleFeatureService;
import org.georchestra.photosobliques.storage.entity.acl.RoleFeatureEntity;
import org.georchestra.photosobliques.storage.repository.acl.RoleFeatureCustomRepository;
import org.georchestra.photosobliques.storage.repository.acl.RoleFeatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * @author FNI18300
 *
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleFeatureServiceImpl implements RoleFeatureService {

	private final RoleFeatureRepository roleFeatureRepository;

	private final RoleFeatureCustomRepository roleFeatureCustomRepository;

	private final RoleFeatureMapper roleFeatureMapper;

	private final RoleFeatureValidator roleFeatureValidator;

	private final RoleFeatureProcessor roleFeatureProcessor;

	@Override
	public List<RoleFeature> searchRolesFeature(RoleFeaturePhotosObliquesCriteria searchCriteria) {
		return roleFeatureMapper.entitiesToDto(roleFeatureCustomRepository.searchRolesFeature(searchCriteria));
	}

	@Override
	@Transactional(readOnly = false)
	public RoleFeature createRoleFeature(RoleFeature roleFeature) throws AppServiceException {
		roleFeatureValidator.validateCreation(roleFeature);
		RoleFeatureEntity entity = roleFeatureMapper.dtoToEntity(roleFeature);
		roleFeatureProcessor.processBeforeCreate(entity, roleFeature);
		entity = roleFeatureRepository.save(entity);
		return roleFeatureMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public RoleFeature updateRoleFeature(RoleFeature roleFeature) throws AppServiceException {
		roleFeatureValidator.validateUpdate(roleFeature);
		RoleFeatureEntity existingEntity = getFeatureEntity(roleFeature.getUuid());
		roleFeatureMapper.dtoToEntity(roleFeature, existingEntity);
		roleFeatureProcessor.processBeforeUpdate(existingEntity, roleFeature);

		roleFeatureRepository.save(existingEntity);
		return roleFeatureMapper.entityToDto(existingEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRoleFeature(UUID uuid) throws AppServiceNotFoundException {
		RoleFeatureEntity entity = getFeatureEntity(uuid);
		roleFeatureRepository.delete(entity);
	}

	@Override
	public RoleFeature getRoleFeature(UUID uuid) throws AppServiceNotFoundException {
		return roleFeatureMapper.entityToDto(getFeatureEntity(uuid));
	}

	@Nonnull
	private RoleFeatureEntity getFeatureEntity(UUID uuid) throws AppServiceNotFoundException {
		if (uuid == null) {
			throw new IllegalArgumentException("UUID required");
		}
		RoleFeatureEntity entity = roleFeatureRepository.findByUuid(uuid);
		if (entity == null) {
			throw new AppServiceNotFoundException(RoleFeatureEntity.class, uuid);
		}
		return entity;
	}
}
