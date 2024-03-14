package org.georchestra.photosobliques.service.sm.photo;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.helper.common.GeometryHelper;
import org.georchestra.photosobliques.service.mapper.PhotoObliqueMapper;
import org.georchestra.photosobliques.service.sm.configuration.ConfigurationService;
import org.georchestra.photosobliques.storage.entity.PhotoObliqueEntity;
import org.georchestra.photosobliques.storage.repository.photo.PhotoObliqueCustomRepository;
import org.georchestra.photosobliques.storage.repository.photo.PhotoObliqueRepository;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FNI18300
 *
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoObliqueServiceImpl implements PhotoObliqueService {

	private final PhotoObliqueRepository photoObliqueRepository;
	private final PhotoObliqueCustomRepository photoObliqueCustomRepository;
	private final PhotoObliqueMapper photoObliqueMapper;
	private final GeometryHelper geometryHelper;
	private final ConfigurationService configurationService;

	@Override
	public Page<PhotoOblique> searchPhotoOblique(PhotoObliqueSearchCriteria photoObliqueSearchCriteria, Pageable pageable) throws AppServiceException {
		Geometry geometry = geometryHelper.convertGeometry(photoObliqueSearchCriteria.getGeometry());
		if(geometry == null) {
			throw new AppServiceException("Aucune géométrie n'a été trouvée");
		}
		List<Tuple> photosObliquesEntities = photoObliqueCustomRepository.searchPhotosObliquesWithRelevance(photoObliqueSearchCriteria,
				configurationService.getApplicationConfiguration().getToleranceAngle(), pageable);

		return mapAndFillPhotoOblique(photosObliquesEntities, pageable);
	}

	private Page<PhotoOblique> mapAndFillPhotoOblique(List<Tuple> photosObliquesEntities, Pageable pageable) {
		List<PhotoOblique> photoObliques = new ArrayList<>();

		for (Tuple tuple : photosObliquesEntities) {
			PhotoObliqueEntity entity = (PhotoObliqueEntity) tuple.get(0);
			PhotoOblique photoOblique = photoObliqueMapper.entityToDto(entity, configurationService);

			photoOblique.setRelevance((Double) tuple.get(1));
			photoObliques.add(photoOblique);
		}

		return new PageImpl<>(photoObliques, pageable, -1);
	}


	@Override
	public Integer countPhotoObliques(PhotoObliqueSearchCriteria photoObliqueSearchCriteria) throws AppServiceException {
		return photoObliqueCustomRepository.countPhotosObliques(photoObliqueSearchCriteria, configurationService.getApplicationConfiguration().getToleranceAngle());
	}

	@Override
	public List<Integer> searchDates(PhotoObliqueSearchCriteria photoObliqueSearchCriteria) throws AppServiceException {
		//TODO
		return new ArrayList<>();
	}
}