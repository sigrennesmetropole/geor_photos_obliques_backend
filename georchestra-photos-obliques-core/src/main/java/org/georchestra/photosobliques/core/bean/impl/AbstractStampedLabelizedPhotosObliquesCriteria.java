package org.georchestra.photosobliques.core.bean.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.bean.StampedPhotosObliquesCriteria;

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
public abstract class AbstractStampedLabelizedPhotosObliquesCriteria extends AbstractLabelizedPhotosObliquesCriteria
		implements StampedPhotosObliquesCriteria {

	private Boolean active;

}
