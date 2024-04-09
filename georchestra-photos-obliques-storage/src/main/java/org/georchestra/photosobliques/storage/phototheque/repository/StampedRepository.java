/**
 *
 */
package org.georchestra.photosobliques.storage.phototheque.repository;

import org.georchestra.photosobliques.storage.common.entity.AbstractLongIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@NoRepositoryBean
public interface StampedRepository<T extends AbstractLongIdEntity> extends JpaRepository<T, Long>, Serializable {

	List<T> findActive(LocalDateTime d);

	T findByUuid(UUID uuid);

	List<T> findByCode(String code, Boolean active);

	T findFirstByCode(String code, Boolean active);

}
