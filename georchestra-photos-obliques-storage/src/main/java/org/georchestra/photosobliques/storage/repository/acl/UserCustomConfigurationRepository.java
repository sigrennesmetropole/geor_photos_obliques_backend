package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.storage.entity.acl.UserCustomConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Dao pour les configuration custom
 * 
 * @author FNI18300
 *
 */
@Repository
public interface UserCustomConfigurationRepository extends JpaRepository<UserCustomConfigurationEntity, Long>, Serializable {
	
	UserCustomConfigurationEntity findByLogin(String login);
}
