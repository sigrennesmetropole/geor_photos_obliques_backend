/**
 * 
 */
package org.georchestra.photosobliques.storage.repository.acl;

import org.georchestra.photosobliques.storage.entity.acl.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author FNI18300
 *
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

	/**
	 * Test l'existance d'un token
	 *
	 * @param token
	 * @return le token de rafraichissement
	 */
	RefreshTokenEntity findFirstByTokenEquals(String token);

}
