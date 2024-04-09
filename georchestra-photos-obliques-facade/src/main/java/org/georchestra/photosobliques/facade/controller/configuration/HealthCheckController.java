/**
 * SHOM - SEARCH
 */
package org.georchestra.photosobliques.facade.controller.configuration;

import org.georchestra.photosobliques.facade.controller.api.HealthCheckApi;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FNI18300
 */
@RestController
public class HealthCheckController implements HealthCheckApi, HealthIndicator {

    @Override
    public ResponseEntity<Void> checkHealth() {
        return ResponseEntity.ok().build();
    }

    @Override
    public Health health() {
        return Health.up().build();
    }

}
