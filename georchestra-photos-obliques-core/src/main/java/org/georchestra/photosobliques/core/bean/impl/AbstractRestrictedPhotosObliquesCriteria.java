package org.georchestra.photosobliques.core.bean.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.bean.RestrictedPhotosObliquesCriteria;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public abstract class AbstractRestrictedPhotosObliquesCriteria implements RestrictedPhotosObliquesCriteria {

	/**
	 * si restricted = false cela signifie que seul est les éléments non restreints
	 * (NP) sont accessible <br/>
	 * si restricted = true cela signie que tous les éléments NP + DR sont
	 * accessibles
	 */
	private boolean restricted;

}
