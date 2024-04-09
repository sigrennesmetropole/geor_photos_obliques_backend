package org.georchestra.photosobliques.facade.controller.phototheque;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.facade.controller.api.ProvidersApi;
import org.georchestra.photosobliques.facade.util.AbstractController;
import org.georchestra.photosobliques.service.sm.photo.PhotoObliqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author NCA20245
 */
@RestController
@AllArgsConstructor
public class ProvidersApiController extends AbstractController implements ProvidersApi {

    private final PhotoObliqueService photoObliqueService;


    @Override
    public ResponseEntity<List<String>> availableProviders(String geometry) throws Exception {
        return ResponseEntity.ok(photoObliqueService.searchProviders(geometry));
    }

}

