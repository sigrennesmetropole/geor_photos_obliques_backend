/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.service.processor;

import org.georchestra.photosobliques.service.exception.AppServiceBadRequestException;

/**
 * @author FNI18300
 */
public interface EntityProcessor<E, D> {

	void processBeforeCreate(E entity, D dto) throws AppServiceBadRequestException;

	void processBeforeUpdate(E entity, D dto) throws AppServiceBadRequestException;

}
