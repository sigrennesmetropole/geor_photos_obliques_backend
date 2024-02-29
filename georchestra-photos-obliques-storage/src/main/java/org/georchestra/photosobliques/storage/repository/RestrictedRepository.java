/**
 *
 */
package org.georchestra.photosobliques.storage.repository;

import org.georchestra.photosobliques.core.common.Restrictable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@NoRepositoryBean
public interface RestrictedRepository<E extends Restrictable> extends JpaRepository<E, Long> {

	@Nullable
	E findByUuidAndRestrictedIsNot(UUID uuid, Boolean restricted);

}
