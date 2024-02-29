package org.georchestra.photosobliques.core.bean.acl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.georchestra.photosobliques.core.bean.FeatureScope;

import java.util.UUID;

/**
 * @author NCA20245
 *
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class RoleFeaturePhotosObliquesCriteria {

    private FeatureScope scope;

    private UUID roleUuid;

}
