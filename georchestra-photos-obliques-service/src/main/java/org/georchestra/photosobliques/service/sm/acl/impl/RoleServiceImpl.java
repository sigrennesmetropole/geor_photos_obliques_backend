/**
 *
 */
package org.georchestra.photosobliques.service.sm.acl.impl;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.georchestra.photosobliques.core.bean.Role;
import org.georchestra.photosobliques.core.bean.acl.RolePhotosObliquesCriteria;
import org.georchestra.photosobliques.service.mapper.acl.RoleMapper;
import org.georchestra.photosobliques.service.sm.acl.RoleService;
import org.georchestra.photosobliques.storage.entity.acl.RoleEntity;
import org.georchestra.photosobliques.storage.repository.acl.RoleCustomRepository;
import org.georchestra.photosobliques.storage.repository.acl.RoleRepository;

/**
 * Service de gestion des roles d'utilisateurs
 *
 * @author MCY12700
 *
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	private final RoleCustomRepository roleCustomRepository;

	private final RoleMapper roleMapper;

	@Override
	public List<Role> searchRoles(RolePhotosObliquesCriteria searchCriteria) {
		return roleMapper.entitiesToDto(roleCustomRepository.searchRoles(searchCriteria));
	}

	@Override
	public Role getRole(UUID uuid) {
		if (uuid == null) {
			throw new IllegalArgumentException("UUID required");
		}
		RoleEntity entity = roleRepository.findByUuid(uuid);
		return roleMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public Role createRole(Role role) {
		RoleEntity entity = roleMapper.dtoToEntity(role);
		entity.setUuid(UUID.randomUUID());
		roleRepository.save(entity);
		return roleMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public Role updateRole(Role role) {
		if (role.getUuid() == null) {
			throw new IllegalArgumentException("UUID manquant");
		}
		RoleEntity entity = roleRepository.findByUuid(role.getUuid());
		if (entity == null) {
			throw new IllegalArgumentException("Resource inexistante:" + role.getUuid());
		}
		roleMapper.dtoToEntity(role, entity);
		entity = roleRepository.save(entity);
		return roleMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRole(UUID uuid) {
		if (uuid == null) {
			throw new IllegalArgumentException("UUID required");
		}
		RoleEntity entity = roleRepository.findByUuid(uuid);
		roleRepository.delete(entity);
	}

}
