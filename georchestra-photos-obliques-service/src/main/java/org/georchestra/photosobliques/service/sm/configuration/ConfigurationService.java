package org.georchestra.photosobliques.service.sm.configuration;

import org.georchestra.photosobliques.core.bean.ApplicationConfiguration;

/**
 * Interface de gestion de la configuration
 */
public interface ConfigurationService {

	/**
	 * @return application information
	 */
	ApplicationConfiguration getApplicationConfiguration();

}
