/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.processor;

import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;
import org.georchestra.photosobliques.storage.entity.AbstractStampedEntity;

import java.time.LocalDateTime;

/**
 * @author FNI18300
 *
 */
public class AbstractStampedEntityProcessor<E extends AbstractStampedEntity, D> extends AbstractEntityProcessor<E, D> {
	@Override
	public void processBeforeCreate(E entity, D dto) throws AppServiceBadRequestException {
		super.processBeforeCreate(entity, dto);
		if (entity.getOpeningDate() == null) {
			entity.setOpeningDate(LocalDateTime.now());
		}
	}
}
