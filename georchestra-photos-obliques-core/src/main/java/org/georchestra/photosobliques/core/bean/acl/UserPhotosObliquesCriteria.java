/**
 * Georchestra - Photos Obliques
 */
package org.georchestra.photosobliques.core.bean.acl;

import lombok.*;
import org.georchestra.photosobliques.core.bean.UserType;

import java.util.List;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserPhotosObliquesCriteria {

	private String login;

	private String firstname;

	private String lastname;

	private String fullname;

	private UserType type;

	private List<UUID> roleUuids;

	private List<String> roleCodes;

}
