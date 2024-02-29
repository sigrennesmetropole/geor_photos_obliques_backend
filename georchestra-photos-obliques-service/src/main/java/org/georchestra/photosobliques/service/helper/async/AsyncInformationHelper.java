/**
 *
 */
package org.georchestra.photosobliques.service.helper.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.storage.entity.async.AsyncInformationEntity;
import org.georchestra.photosobliques.storage.entity.async.AsyncStatus;
import org.georchestra.photosobliques.storage.repository.async.AsyncInformationRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncInformationHelper {

	public static final int PROGRESSION_START = 0;

	public static final int PROGRESSION_DONE = 100;

	private final ACLHelper utilContextHelper;

	private final AsyncInformationRepository asyncInformationRepository;

	public AsyncInformationEntity createAsyncInformation(String scope) {
		AuthenticatedUser user = utilContextHelper.getAuthenticatedUser();
		AsyncInformationEntity asyncInformation = new AsyncInformationEntity();
		asyncInformation.setUuid(UUID.randomUUID());
		asyncInformation.setStatus(AsyncStatus.getInitialStatus());
		asyncInformation.setCreationDate(new Date());
		asyncInformation.setLogin(user != null ? user.getLogin() : "unknown");
		asyncInformation.setProgression(PROGRESSION_START);
		asyncInformation.setScope(scope);
		asyncInformationRepository.save(asyncInformation);
		return asyncInformation;
	}

	public AsyncInformationEntity loadAsyncInformation(UUID uuid) {
		return asyncInformationRepository.findByUuid(uuid);
	}

	public void updateAsyncInformationContent(UUID asyncInformationUuid, DocumentContent documentContent) {
		AsyncInformationEntity asyncInformation = loadAsyncInformation(asyncInformationUuid);
		if (asyncInformation == null) {
			log.warn("Recreate asyncInformation {}", asyncInformationUuid);
			asyncInformation = createAsyncInformation("error");
		}
		updateAsyncInformationContent(asyncInformation, documentContent);
	}

	public void updateAsyncInformationContent(AsyncInformationEntity asyncInformation,
			DocumentContent documentContent) {
		asyncInformation.setStatus(AsyncStatus.DONE);
		asyncInformation.setProgression(100);
		asyncInformation.setContentType(documentContent.getContentType());
		asyncInformation.setFileName(documentContent.getFileName());
		if (documentContent.isFile()) {
			asyncInformation.setFilePath(documentContent.getFile().getAbsolutePath());
		} else {
			throw new IllegalArgumentException("AsyncInformation handle only DocumentContent.isFile==true");
		}
		asyncInformationRepository.save(asyncInformation);
	}

	public void updateAsyncInformationError(UUID asyncInformationUuid, String message) {
		AsyncInformationEntity asyncInformation = loadAsyncInformation(asyncInformationUuid);
		if (asyncInformation == null) {
			log.warn("Recreate asyncInformation {}", asyncInformationUuid);
			asyncInformation = createAsyncInformation("error");
		}
		updateAsyncInformationError(asyncInformation, message);
	}

	protected void updateAsyncInformationError(AsyncInformationEntity asyncInformation, String message) {
		asyncInformation.setStatus(AsyncStatus.ERROR);
		asyncInformation.setProgression(PROGRESSION_DONE);
		asyncInformation.setMessage(message);
		asyncInformationRepository.save(asyncInformation);
	}
}
