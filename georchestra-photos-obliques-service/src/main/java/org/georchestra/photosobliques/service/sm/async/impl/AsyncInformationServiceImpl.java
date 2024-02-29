/**
 *
 */
package org.georchestra.photosobliques.service.sm.async.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.georchestra.photosobliques.core.bean.AsyncInformation;
import org.georchestra.photosobliques.core.bean.ReportInformation;
import org.georchestra.photosobliques.core.common.DocumentContent;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.service.exception.AppServiceException;
import org.georchestra.photosobliques.service.exception.AppServiceNotFoundException;
import org.georchestra.photosobliques.service.exception.AsyncInformationException;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.service.mapper.async.AsyncInformationMapper;
import org.georchestra.photosobliques.service.sm.async.AsyncInformationService;
import org.georchestra.photosobliques.storage.entity.async.AsyncInformationEntity;
import org.georchestra.photosobliques.storage.entity.async.AsyncStatus;
import org.georchestra.photosobliques.storage.repository.async.AsyncInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author FNI18300
 *
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AsyncInformationServiceImpl implements AsyncInformationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncInformationServiceImpl.class);

	@Value("${photosobliques.async.done.retention:300}")
	private int doneRetention;

	@Value("${photosobliques.async.inprogress.retention:180}")
	private int inProgressRetention;

	private final AsyncInformationRepository asyncInformationRepository;

	private final ACLHelper utilContextHelper;

	private final AsyncInformationMapper asyncInformationMapper;

	private final ObjectMapper objectMapper;

	@Override
	public AsyncInformation getAsyncInformation(UUID uuid) throws AppServiceException {
		AsyncInformationEntity entity = getAsyncInformationEntity(uuid);
		return asyncInformationMapper.entityToDto(entity);
	}

	@Override
	public DocumentContent extractAsyncInformationContent(UUID uuid) throws AppServiceException {
		AsyncInformationEntity entity = getAsyncInformationEntity(uuid);
		if (entity.getStatus() != AsyncStatus.DONE) {
			throw new AsyncInformationException("L'élément n'est pas encore généré");
		}
		return new DocumentContent(entity.getFileName(), entity.getContentType(), new File(entity.getFilePath()));
	}

	@Override
	public ReportInformation extractAsyncInformationReport(UUID uuid) throws AppServiceException, IOException {
		DocumentContent content = extractAsyncInformationContent(uuid);
		if( content != null) {
			return objectMapper.readerFor(ReportInformation.class).readValue(content.getFile());
		}
		return null;
	}

	private AsyncInformationEntity getAsyncInformationEntity(UUID uuidAsyncInformation)
			throws AppServiceNotFoundException {

		// Récupération de la procédure
		AsyncInformationEntity entity = asyncInformationRepository.findByUuid(uuidAsyncInformation);

		if (entity == null) {
			throw new AppServiceNotFoundException(AsyncInformationEntity.class, uuidAsyncInformation);
		}

		AuthenticatedUser user = utilContextHelper.getAuthenticatedUser();
		if (user == null || !user.getLogin().equalsIgnoreCase(entity.getLogin())) {
			throw new AccessDeniedException(
					"Aucun utilisateur connecté ou le document n'appartient pas à l'utilisateur connecté");
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = false)
	public void cleanUp() {
		Date d = new Date();
		Date d2 = DateUtils.addMinutes(d, doneRetention * -1);
		List<AsyncInformationEntity> asyncInformations = asyncInformationRepository
				.findByStatusAndCreationDateLessThan(AsyncStatus.DONE, d2);
		deleteAsyncInformation(asyncInformations);

		Date d3 = DateUtils.addMinutes(d, inProgressRetention * -1);
		asyncInformations = asyncInformationRepository.findByStatusAndCreationDateLessThan(AsyncStatus.IN_PROGRESS, d3);
		deleteAsyncInformation(asyncInformations);
	}

	private void deleteAsyncInformation(List<AsyncInformationEntity> asyncInformations) {
		if (CollectionUtils.isNotEmpty(asyncInformations)) {
			for (AsyncInformationEntity asyncInformation : asyncInformations) {
				LOGGER.debug("Handle asyncInformation: {}/{}/{}", asyncInformation.getUuid(),
						asyncInformation.getStatus(), asyncInformation.getScope());
				if (asyncInformation.getFilePath() != null) {
					try {
						Path f = Paths.get(asyncInformation.getFilePath());
						if (Files.deleteIfExists(f)) {
							LOGGER.debug("Handle asyncInformation: {} delete file done", asyncInformation.getUuid());
						}
					} catch (Exception e) {
						LOGGER.warn("Handle asyncInformation: {} failed to delete file {}", asyncInformation.getUuid(),
								asyncInformation.getFilePath());
					}
				}
				asyncInformationRepository.delete(asyncInformation);
			}
		}
	}


}
