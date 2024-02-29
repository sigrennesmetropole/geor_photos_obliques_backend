package org.georchestra.photosobliques.core.bean.impl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.bean.AuditablePhotosObliquesCriteria;

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
public abstract class AbstractAuditablePhotosObliquesCriteria implements AuditablePhotosObliquesCriteria {

	private String createdBy;

	private String lastModifiedBy;

}
