/**
 * SHOM - SEARCH
 */
package org.georchestra.photosobliques.facade.controller.configuration;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.ApplicationConfiguration;
import org.georchestra.photosobliques.facade.controller.api.ConfigurationApi;
import org.georchestra.photosobliques.service.sm.configuration.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FNI18300
 */
@RestController
@AllArgsConstructor
public class ConfigurationController implements ConfigurationApi {

    private final ConfigurationService configurationService;

    @Override
    public ResponseEntity<ApplicationConfiguration> getConfiguration() {
        return ResponseEntity.ok(configurationService.getApplicationConfiguration());
    }
}
