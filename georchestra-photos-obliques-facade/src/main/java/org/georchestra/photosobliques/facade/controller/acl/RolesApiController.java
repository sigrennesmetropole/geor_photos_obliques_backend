/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.facade.controller.acl;

import lombok.AllArgsConstructor;
import org.georchestra.photosobliques.core.bean.Role;
import org.georchestra.photosobliques.core.bean.acl.RolePhotosObliquesCriteria;
import org.georchestra.photosobliques.service.sm.acl.RoleService;
import org.georchestra.photosobliques.facade.controller.api.RolesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@RestController
@AllArgsConstructor
public class RolesApiController implements RolesApi {

	private final RoleService roleService;

	@Override
	public ResponseEntity<List<Role>> searchRoles(@Valid Boolean active) {
		return ResponseEntity.ok(roleService.searchRoles(RolePhotosObliquesCriteria.builder().active(active).build()));
	}

	@Override
	public ResponseEntity<Role> getRole(UUID roleUuid) {
		return ResponseEntity.ok(roleService.getRole(roleUuid));
	}

	@Override
	@PreAuthorize("hasRole('ADMINISTRATOR')|| hasPermission('ROLE','WRITE')")
	public ResponseEntity<Role> createRole(@Valid Role role) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(role));
	}

	@Override
	@PreAuthorize("hasRole('ADMINISTRATOR')|| hasPermission('ROLE','WRITE')")
	public ResponseEntity<Role> updateRole(@Valid Role role) {
		return ResponseEntity.ok(roleService.updateRole(role));
	}

}
