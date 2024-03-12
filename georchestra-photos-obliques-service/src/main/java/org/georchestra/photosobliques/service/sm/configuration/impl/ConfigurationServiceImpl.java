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

	@Value("${server.port}")
	private String serverPort;

	@Value("${photos-obliques.tolerance.angle:20}")
	private Double configToleranceAngle;

	@Value("${photos-obliques.url.vignette}")
	private String confUrlVignette;

	@Value("${photos-obliques.url.apercu}")
	private String confUrlApercu;

	@Value("${photos-obliques.acces.photohd}")
	private String confAccesPhotosHD;

	@Value("${photos-obliques.panier.max.photos:200}")
	private Integer maxPhotos;

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
		result.setToleranceAngle(configToleranceAngle);
		result.setUrlOverview(confUrlApercu);
		result.setUrlThumbnail(confUrlVignette);
		result.setAccesPhotosHD(confAccesPhotosHD);
		result.setServerPort(serverPort);
		result.setMaxCartSize(maxPhotos);

		return result;

	}

}
