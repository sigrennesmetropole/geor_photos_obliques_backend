/**
 *
 */
package org.georchestra.photosobliques.service.sm.async;

import java.io.IOException;
import java.util.UUID;

import org.georchestra.photosobliques.core.bean.AsyncInformation;
import org.georchestra.photosobliques.core.bean.ReportInformation;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.service.exception.AppServiceException;

/**
 * @author FNI18300
 *
 */
public interface AsyncInformationService {

	AsyncInformation getAsyncInformation(UUID uuid) throws AppServiceException;

	DocumentContent extractAsyncInformationContent(UUID uuid) throws AppServiceException;

	ReportInformation extractAsyncInformationReport(UUID uuid) throws AppServiceException, IOException;

	void cleanUp();
}
