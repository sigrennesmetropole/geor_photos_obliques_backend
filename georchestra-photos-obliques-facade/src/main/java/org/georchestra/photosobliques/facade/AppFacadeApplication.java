/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe de configuration globale de l'application.
 */
@SpringBootApplication(scanBasePackages = { "org.georchestra.photosobliques.facade", "org.georchestra.photosobliques.service",
		"org.georchestra.photosobliques.storage", "org.georchestra.photosobliques.core" })
@PropertySource(value = { "classpath:photos-obliques-common.properties" })
@PropertySource(value = { "file:${georchestra.datadir}/default.properties" }, ignoreResourceNotFound = false)
@PropertySource(value = { "file:${georchestra.datadir}/photos-obliques/photos-obliques.properties" }, ignoreResourceNotFound = false)
@EnableScheduling
@EntityScan("org.georchestra.photosobliques.storage")
public class AppFacadeApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {

		// Renomage du fichier de properties pour éviter les conflits avec d'autres
		// applications
		System.setProperty("spring.config.name", "georchestra-photos-obliques-backend");
		System.setProperty("spring.devtools.restart.enabled", "false");
		System.setProperty("org.geotools.http.HTTPClientFactory",
				"org.georchestra.photosobliques.service.helper.map.GeotoolsClientFactory");
		SpringApplication.run(AppFacadeApplication.class, args);

	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(AppFacadeApplication.class);
	}

}
