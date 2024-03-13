package org.georchestra.photosobliques.service.sm.photo;

import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoObliqueService {

    Page<PhotoOblique> searchPhotoOblique(PhotoObliqueSearchCriteria photoObliqueSearchCriteria, Pageable pageable) throws AppServiceException;

    Integer countPhotoObliques(PhotoObliqueSearchCriteria photoObliqueSearchCriteria) throws AppServiceException;

    List<Integer> searchDates(PhotoObliqueSearchCriteria photoObliqueSearchCriteria) throws  AppServiceException;

}
