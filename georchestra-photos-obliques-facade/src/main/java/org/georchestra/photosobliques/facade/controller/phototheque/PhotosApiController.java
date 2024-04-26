package org.georchestra.photosobliques.facade.controller.phototheque;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.PhotosCount;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.facade.controller.api.PhotosApi;
import org.georchestra.photosobliques.facade.helper.ControllerHelper;
import org.georchestra.photosobliques.facade.util.AbstractController;
import org.georchestra.photosobliques.facade.util.UtilPageable;
import org.georchestra.photosobliques.service.sm.photo.PhotoObliqueService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NCA20245
 *
 */
@RestController
@AllArgsConstructor
public class PhotosApiController extends AbstractController implements PhotosApi {

    private final PhotoObliqueService photoObliqueService;
    private final ControllerHelper controllerHelper;

    private final UtilPageable utilPageable;

    @Override
    public ResponseEntity<PhotosCount> countPhotos(String geometry, Integer startDate, Integer endDate, Double angleDegre, String provider, String owner) throws Exception {
        PhotoObliqueSearchCriteria searchCriteria = new PhotoObliqueSearchCriteria(geometry, startDate, endDate, angleDegre, provider, owner);

        Integer count = photoObliqueService.countPhotoObliques(searchCriteria);
        PhotosCount photosCount = new PhotosCount();
        photosCount.setNumberOfResult(count);
        return ResponseEntity.ok(photosCount);
    }

    @Override
    public ResponseEntity<Resource> downloadPhotos(List<String> photoIds, String zipName, String prefix) throws Exception {
        DocumentContent zipContent = photoObliqueService.downloadPhotos(photoIds, zipName, prefix);

        return controllerHelper.downloadableResponseEntity(zipContent);
    }

    @Override
    public ResponseEntity<List<PhotoOblique>> searchPhotos(String geometry, Integer startDate, Integer endDate, Double angleDegre, String provider,
                                                              String owner, Integer offset,  Integer limit, String order) throws Exception {

        Pageable pageable = utilPageable.getPageable(offset, limit, order);

        PhotoObliqueSearchCriteria searchCriteria = new PhotoObliqueSearchCriteria(geometry, startDate, endDate, angleDegre, provider, owner);

        List<PhotoOblique> photoObliques = photoObliqueService.searchPhotoOblique(searchCriteria, pageable);
        return ResponseEntity.ok(photoObliques);
    }
}

