package org.georchestra.photosobliques.service.sm.configuration.impl;

import org.georchestra.photosobliques.core.bean.ApplicationConfiguration;
import org.georchestra.photosobliques.service.sm.configuration.ConfigurationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Value("${application.version}")
	private String applicationVersion;

	@Value("${application.comment}")
	private String applicationComment;

	@Value("${photos_obliques.geoserver.workspace:photos_obliques}")
	private String geoserverWorkspace;

	@Value("${photos_obliques.geoserver.namespace:http://www.georchestra.fr/photos_obliques}")
	private String geoserverNamespace;

	@Value("${photos_obliques.geoserver.srid.default:EPSG:3948}")
	private String geoserverDefaultSRID;

	/**
	 * Permet de récupérer la configuration
	 *
	 * @return
	 */
	@Override
	public ApplicationConfiguration getApplicationConfiguration() {
		ApplicationConfiguration result = new ApplicationConfiguration();

		result.setVersion(applicationVersion);
		result.setComment(applicationComment);
		result.setGeoserverWorkspace(geoserverWorkspace);
		result.setGeoserverNamespace(geoserverNamespace);
		result.setGeoserverDefaultSRID(geoserverDefaultSRID);

		return result;

	}

}
