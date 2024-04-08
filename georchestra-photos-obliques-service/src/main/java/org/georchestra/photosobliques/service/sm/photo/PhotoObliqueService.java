package org.georchestra.photosobliques.service.sm.photo;

import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoObliqueService {

    List<PhotoOblique> searchPhotoOblique(PhotoObliqueSearchCriteria photoObliqueSearchCriteria, Pageable pageable) throws AppServiceException;

    Integer countPhotoObliques(PhotoObliqueSearchCriteria photoObliqueSearchCriteria) throws AppServiceException;

    List<String> searchOwners(String geometryWKT)throws AppServiceException;

    List<String> searchProviders(String geometryWKT)throws AppServiceException;

    List<Integer> searchYears(String geometryWKT)throws AppServiceException;

    DocumentContent downloadPhotos(List<String> photoIds, String zipName, String prefix) throws AppServiceException;
}
