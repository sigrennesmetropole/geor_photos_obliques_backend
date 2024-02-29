/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade.util;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author FNI18300
 *
 */
public class AbstractController {

	@Autowired
	@Getter(value = AccessLevel.PROTECTED)
	private UtilPageable utilPageable;

}
