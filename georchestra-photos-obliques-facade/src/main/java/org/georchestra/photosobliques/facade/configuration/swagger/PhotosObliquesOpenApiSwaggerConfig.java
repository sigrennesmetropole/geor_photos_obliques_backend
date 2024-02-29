package org.georchestra.photosobliques.facade.configuration.swagger;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhotosObliquesOpenApiSwaggerConfig extends OpenApiSwaggerConfig {

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("photos-obliques-back").packagesToScan("org.georchestra.photosobliques.facade.controller")
				.build();
	}

}
