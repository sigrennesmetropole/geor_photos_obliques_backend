package org.georchestra.photosobliques.facade.controller.photos;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.PhotoOblique;
import org.georchestra.photosobliques.core.bean.PhotosCount;
import org.georchestra.photosobliques.core.bean.PhotosObliquesPageResult;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.facade.controller.api.PhotosApi;
import org.georchestra.photosobliques.facade.util.AbstractController;
import org.georchestra.photosobliques.facade.util.UtilPageable;
import org.georchestra.photosobliques.service.sm.photo.PhotoObliqueService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author NCA20245
 *
 */
@RestController
@AllArgsConstructor
public class PhotosApiController extends AbstractController implements PhotosApi {

    private final PhotoObliqueService photoObliqueService;

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
        return PhotosApi.super.downloadPhotos(photoIds, zipName, prefix);
    }

    @Override
    public ResponseEntity<PhotosObliquesPageResult> searchPhotos(String geometry, Integer startDate, Integer endDate, Double angleDegre, String provider,
                                                              String owner, Integer offset,  Integer limit, String order) throws Exception {

        Pageable pageable = utilPageable.getPageable(offset, limit, order);

        PhotoObliqueSearchCriteria searchCriteria = new PhotoObliqueSearchCriteria(geometry, startDate, endDate, angleDegre, provider, owner);

        Page<PhotoOblique> page = photoObliqueService.searchPhotoOblique(searchCriteria, pageable);
        PhotosObliquesPageResult result = new PhotosObliquesPageResult();
        result.setElements(page.getContent());
        result.setTotal(page.getTotalElements());
        return ResponseEntity.ok(result);
    }
}

