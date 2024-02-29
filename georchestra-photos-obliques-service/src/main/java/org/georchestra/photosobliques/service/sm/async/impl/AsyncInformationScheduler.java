/**
 *
 */
package org.georchestra.photosobliques.service.sm.async.impl;

import org.georchestra.photosobliques.service.sm.async.AsyncInformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author FNI18300
 *
 */
@Component
public class AsyncInformationScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncInformationScheduler.class);

	@Autowired
	private AsyncInformationService asyncInformationService;

	@Scheduled(fixedDelayString = "${photos_obliques.async.delay:300000}")
	public void scheduleAsyncInformationCleaner() {
		LOGGER.info("Start {}...", getClass().getSimpleName());
		asyncInformationService.cleanUp();
		LOGGER.info("Start {} done.", getClass().getSimpleName());
	}
}
