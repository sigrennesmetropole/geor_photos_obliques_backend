/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.processor;

import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;

import java.util.UUID;

/**
 * @author FNI18300
 */
public abstract class AbstractEntityProcessor<E extends AbstractLongIdEntity, D> implements EntityProcessor<E, D> {

	@Override
	public void processBeforeCreate(E entity, D dto) throws AppServiceBadRequestException {
		entity.setUuid(UUID.randomUUID());
	}

	@Override
	public void processBeforeUpdate(E entity, D dto) throws AppServiceBadRequestException {
		// default implementation
	}

}
