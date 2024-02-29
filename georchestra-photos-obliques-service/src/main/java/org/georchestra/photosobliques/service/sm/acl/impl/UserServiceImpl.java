/**
 *
 */
package org.georchestra.photosobliques.service.sm.acl.impl;

import java.util.UUID;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.User;
import org.georchestra.photosobliques.core.bean.UserCustomConfiguration;
import org.georchestra.photosobliques.core.bean.acl.UserPhotosObliquesCriteria;
import org.georchestra.photosobliques.core.security.AuthenticatedUser;
import org.georchestra.photosobliques.service.helper.acl.ACLHelper;
import org.georchestra.photosobliques.service.helper.acl.UserCustomConfigurationHelper;
import org.georchestra.photosobliques.service.helper.authentification.AuthentificationHelper;
import org.georchestra.photosobliques.service.mapper.acl.UserMapper;
import org.georchestra.photosobliques.service.sm.acl.UserService;
import org.georchestra.photosobliques.storage.entity.acl.UserCustomConfigurationEntity;
import org.georchestra.photosobliques.storage.entity.acl.UserEntity;
import org.georchestra.photosobliques.storage.repository.acl.UserCustomConfigurationRepository;
import org.georchestra.photosobliques.storage.repository.acl.UserCustomRepository;
import org.georchestra.photosobliques.storage.repository.acl.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service de gestion des utilisateurs
 *
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private static final String UUID_USER_MISSING_MESSAGE = "UUID user missing";
	private static final String USER_UNKNOWN_MESSAGE = "User unknown :";

	private final ACLHelper utilContextHelper;

	private final UserRepository userRepository;


	private final UserCustomConfigurationRepository userCustomConfigurationRepository;

	private final UserCustomRepository userCustomRepository;


	private final UserMapper userMapper;

	private final AuthentificationHelper authentificationHelper;


	private UserCustomConfigurationHelper userCustomConfigurationHelper;

	@Override
	public Page<User> searchUsers(UserPhotosObliquesCriteria searchCriteria, Pageable pageable) {
		return userMapper.entitiesToDto(userCustomRepository.searchUsers(searchCriteria, pageable), pageable);
	}

	@Override
	public User getUser(UUID uuid) {
		return userMapper.entityToDto(userRepository.findByUuid(uuid));
	}

	@Override
	@Transactional(readOnly = false)
	public User getMe() {
		return getUserByLogin(authentificationHelper.getUsername());
	}

	@Override
	public User getUserByLogin(String login) {
		UserEntity user = userRepository.findByLogin(login);
		return userMapper.entityToDto(user);
	}

	@Override
	@Transactional(readOnly = false)
	public User createUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User est obligatoire");
		}

		UserEntity entity = userMapper.dtoToEntity(user);
		entity.setUuid(UUID.randomUUID());

		validEntity(entity);
		userRepository.save(entity);
		return userMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public User updateUser(User user) {
		if (user.getUuid() == null) {
			throw new IllegalArgumentException(UUID_USER_MISSING_MESSAGE);
		}
		UserEntity entity = userRepository.findByUuid(user.getUuid());
		if (entity == null) {
			throw new IllegalArgumentException(USER_UNKNOWN_MESSAGE + user.getUuid());
		}
		userMapper.dtoToEntity(user, entity);

		validEntity(entity);
		userRepository.save(entity);
		return userMapper.entityToDto(entity);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUser(UUID uuid) {
		if (uuid == null) {
			throw new IllegalArgumentException(UUID_USER_MISSING_MESSAGE);
		}
		UserEntity entity = userRepository.findByUuid(uuid);
		if (entity == null) {
			throw new IllegalArgumentException(USER_UNKNOWN_MESSAGE + uuid);
		}
		userRepository.delete(entity);
	}

	@Override
	public UserCustomConfiguration getMyConfiguration() {
		AuthenticatedUser authenticatedUser = utilContextHelper.getAuthenticatedUser();
		UserCustomConfigurationEntity userCustomConfiguration = userCustomConfigurationRepository
				.findByLogin(authenticatedUser.getLogin());
		return userCustomConfigurationHelper.mergCustomConfiguration(userCustomConfiguration);
	}

	@Override
	public void updateMyConfiguration(UserCustomConfiguration userCustomConfiguration) {
		AuthenticatedUser authenticatedUser = utilContextHelper.getAuthenticatedUser();
		UserCustomConfigurationEntity entity = userCustomConfigurationRepository
				.findByLogin(authenticatedUser.getLogin());
		if (entity == null) {
			entity = new UserCustomConfigurationEntity();
			entity.setLogin(authenticatedUser.getLogin());
			entity.setUuid(UUID.randomUUID());
		}
		entity.setProperties(userCustomConfigurationHelper.serializeCustomConfiguration(userCustomConfiguration));

		userCustomConfigurationRepository.save(entity);

	}

	/**
	 * Validation d'un User
	 *
	 * @param entity
	 */
	private void validEntity(UserEntity entity) {
		if (StringUtils.isEmpty(entity.getLogin())) {
			throw new IllegalArgumentException("Invalid empty login");
		}
		if (null == entity.getType()) {
			throw new IllegalArgumentException("Invalid empty type");
		}
	}

}
