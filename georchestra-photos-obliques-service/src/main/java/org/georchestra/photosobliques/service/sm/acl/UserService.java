/**
 *
 */
package org.georchestra.photosobliques.service.sm.acl;

import org.georchestra.photosobliques.core.bean.User;
import org.georchestra.photosobliques.core.bean.UserCustomConfiguration;
import org.georchestra.photosobliques.core.bean.acl.UserPhotosObliquesCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service de gestion des utilisateurs
 *
 * @author FNI18300
 *
 */
public interface UserService {

	/**
	 * Charge la liste paginée des utilisateurs en fonction de critères de recherche
	 *
	 * @param searchCriteria
	 * @param pageable
	 * @return liste paginée des utilisateurs
	 */
	Page<User> searchUsers(UserPhotosObliquesCriteria searchCriteria, Pageable pageable);

	/**
	 * Retourne un utilisateur en fonction de son uuid, avec toutes ses propriétés
	 * chargées
	 *
	 * @param uuid
	 * @return
	 */
	User getUser(UUID uuid);

	/**
	 * Retourne l'utilisateur connecté
	 *
	 * @return
	 */
	User getMe();

	/**
	 *
	 * @return le configuration de l'utilisateur connecté
	 */
	UserCustomConfiguration getMyConfiguration();

	/**
	 *
	 * @param userCustomConfiguration
	 */
	void updateMyConfiguration(UserCustomConfiguration userCustomConfiguration);

	/**
	 * Retourn un utilisateur par son login
	 *
	 * @param login le login
	 * @return
	 */
	User getUserByLogin(String login);

	/**
	 * Create a user
	 *
	 * @param user
	 * @return
	 */
	User createUser(User user);

	/**
	 * Update a User entity
	 *
	 * @param user
	 * @return
	 */
	User updateUser(User user);

	/**
	 * Delete a User entity
	 *
	 * @param uuid
	 */
	void deleteUser(UUID uuid);

}
