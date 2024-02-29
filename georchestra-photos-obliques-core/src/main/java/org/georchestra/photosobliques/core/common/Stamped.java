package org.georchestra.photosobliques.core.common;

import java.time.LocalDateTime;

/**
 * Interface des entités possédant un plage temporelle d'existance
 *
 * @author FNI18300
 */
public interface Stamped {

	LocalDateTime getOpeningDate();

	void setOpeningDate(LocalDateTime openingDate);

	LocalDateTime getClosingDate();

	void setClosingDate(LocalDateTime closingDate);
}
