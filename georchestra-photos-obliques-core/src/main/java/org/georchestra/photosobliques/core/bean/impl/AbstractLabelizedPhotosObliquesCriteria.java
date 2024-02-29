package org.georchestra.photosobliques.core.bean.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.bean.CodedPhotosObliquesCriteria;
import org.georchestra.photosobliques.core.bean.LabelizedPhotosObliquesCriteria;

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
public abstract class AbstractLabelizedPhotosObliquesCriteria implements CodedPhotosObliquesCriteria, LabelizedPhotosObliquesCriteria {

	private String code;

	private String label;

}
