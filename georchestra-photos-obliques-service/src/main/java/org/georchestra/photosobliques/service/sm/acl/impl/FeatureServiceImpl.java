package org.georchestra.photosobliques.service.sm.acl.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.georchestra.photosobliques.core.bean.Feature;
import org.georchestra.photosobliques.core.bean.acl.FeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceNotFoundException;
import org.georchestra.photosobliques.service.mapper.acl.FeatureMapper;
import org.georchestra.photosobliques.service.sm.acl.FeatureService;
import org.georchestra.photosobliques.storage.entity.acl.FeatureEntity;
import org.georchestra.photosobliques.storage.repository.acl.FeatureCustomRepository;
import org.georchestra.photosobliques.storage.repository.acl.FeatureRepository;
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
public class FeatureServiceImpl implements FeatureService {

	private final FeatureRepository featureRepository;
	private final FeatureCustomRepository featureCustomRepository;
	private final FeatureMapper featureMapper;
	private final FeatureValidator featureValidator;
	private final FeatureProcessor featureProcessor;

	@Override
	public List<Feature> searchFeatures(FeaturePhotosObliquesCriteria searchCriteria) {
		return featureMapper.entitiesToDto(featureCustomRepository.searchFeatures(searchCriteria));
	}

	@Override
	@Transactional(readOnly = false)
	public Feature createFeature(Feature feature) throws AppServiceException {
		featureValidator.validateCreation(feature);
		FeatureEntity entity = featureMapper.dtoToEntity(feature);
		featureProcessor.processBeforeCreate(entity, feature);
		entity = featureRepository.save(entity);
		return featureMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public Feature updateFeature(Feature feature) throws AppServiceException {
		featureValidator.validateUpdate(feature);
		FeatureEntity existingEntity = getFeatureEntity(feature.getUuid());
		featureMapper.dtoToEntity(feature, existingEntity);
		featureProcessor.processBeforeUpdate(existingEntity, feature);
		featureRepository.save(existingEntity);
		return featureMapper.entityToDto(existingEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteFeature(UUID uuid) throws AppServiceNotFoundException {
		FeatureEntity entity = getFeatureEntity(uuid);
		featureRepository.delete(entity);
	}

	@Override
	public Feature getFeature(UUID uuid) throws AppServiceNotFoundException {
		return featureMapper.entityToDto(getFeatureEntity(uuid));
	}

	@Nonnull
	private FeatureEntity getFeatureEntity(UUID uuid) throws AppServiceNotFoundException {
		if (uuid == null) {
			throw new IllegalArgumentException("UUID required");
		}
		FeatureEntity entity = featureRepository.findByUuid(uuid);
		if (entity == null) {
			throw new AppServiceNotFoundException(FeatureEntity.class, uuid);
		}
		return entity;
	}
}
