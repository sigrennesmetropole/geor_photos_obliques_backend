package org.georchestra.photosobliques.service.config;

import org.georchestra.photosobliques.storage.repository.StampedRepositoryImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "org.georchestra.photosobliques.storage.entity" })
@EnableJpaRepositories(basePackages = {
		"org.georchestra.photosobliques.storage.repository" }, repositoryBaseClass = StampedRepositoryImpl.class)
@EnableJpaAuditing
public class SearchDatabaseConfiguration {

}
