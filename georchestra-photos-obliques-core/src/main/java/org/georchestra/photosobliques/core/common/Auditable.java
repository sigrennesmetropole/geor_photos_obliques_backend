/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.core.common;

import java.time.LocalDateTime;

/**
 * Interface des entités auditables
 *
 * @author FNI18300
 *
 */
public interface Auditable {

	LocalDateTime getCreationDate();

	LocalDateTime getUpdatedDate();

	String getCreatedBy();

	String getUpdatedBy();

}
