package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.storage.entity.acl.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Dao pour les Users
 * 
 * @author FNI18300
 *
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUuid(UUID uuid);

	UserEntity findByLogin(String login);
}
