/**
 *
 */
package org.georchestra.photosobliques.service.sm.acl;

import java.util.List;
import java.util.UUID;

import org.georchestra.photosobliques.core.bean.Role;
import org.georchestra.photosobliques.core.bean.acl.RolePhotosObliquesCriteria;

/**
 * @author MCY12700
 *
 */
public interface RoleService {

	/**
	 * List all Role
	 *
	 * @return providers list
	 */
	List<Role> searchRoles(RolePhotosObliquesCriteria searchCriteria);

	/**
	 * provider
	 *
	 * @return Role list
	 */
	Role getRole(UUID uuid);

	/**
	 * Create a Role
	 *
	 * @param role
	 * @return
	 */
	Role createRole(Role role);

	/**
	 * Update a Role entity
	 *
	 * @param role
	 * @return
	 */
	Role updateRole(Role role);

	/**
	 * Delete a Role entity
	 *
	 * @param uuid
	 */
	void deleteRole(UUID uuid);

}
