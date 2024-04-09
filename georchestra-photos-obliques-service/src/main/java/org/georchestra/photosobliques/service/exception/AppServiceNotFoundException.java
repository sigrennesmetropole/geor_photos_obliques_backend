package org.georchestra.photosobliques.service.exception;

import jakarta.persistence.Table;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.storage.common.entity.AbstractLongIdEntity;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;

/**
 * @author fni18300
 */
public class AppServiceNotFoundException extends AppServiceException {

	private static final String S_WITH_UUID_S_NOT_FOUND = "%s with UUID = \"%s\" not found";

	private static final long serialVersionUID = 5747244696042729320L;

	private <T> AppServiceNotFoundException(Class<T> entityClass, UUID entityUuid, EmptyResultDataAccessException cause) {
		super(getMessage(entityUuid, entityClass), cause, AppServiceExceptionsStatus.NOT_FOUND);
	}

	public <T> AppServiceNotFoundException(Class<T> entityClass, UUID entityUuid) {
		super(getMessage(entityUuid, entityClass), AppServiceExceptionsStatus.NOT_FOUND);
	}

	public <T> AppServiceNotFoundException(Class<T> entityClass, Long entityUuid) {
		super(getMessage(entityUuid, entityClass), AppServiceExceptionsStatus.NOT_FOUND);
	}

	public <T> AppServiceNotFoundException(Class<T> entityClass, String entityUuid) {
		super(getMessage(entityUuid, entityClass), AppServiceExceptionsStatus.NOT_FOUND);
	}

	public <T> AppServiceNotFoundException(AbstractLongIdEntity entity, EmptyResultDataAccessException cause) {
		this(entity.getClass(), entity.getUuid(), cause);
	}

	private static <T> String getMessage(UUID entityUuid, Class<T> entityClass) {
		return String.format(S_WITH_UUID_S_NOT_FOUND, getEntityName(entityClass), entityUuid);
	}

	private static <T> String getMessage(Long entityId, Class<T> entityClass) {
		return String.format(S_WITH_UUID_S_NOT_FOUND, getEntityName(entityClass), entityId);
	}

	private static <T> String getMessage(String entityId, Class<T> entityClass) {
		return String.format(S_WITH_UUID_S_NOT_FOUND, getEntityName(entityClass), entityId);
	}

	private static <T> String getEntityName(Class<T> entityClass) {
		val tableAnnotation = entityClass.getAnnotation(Table.class);
		if (tableAnnotation != null) {
			final String tableName = tableAnnotation.name();
			if (StringUtils.isNotEmpty(tableName)) {
				return tableName;
			}
		}
		return entityClass.getSimpleName();
	}

}
