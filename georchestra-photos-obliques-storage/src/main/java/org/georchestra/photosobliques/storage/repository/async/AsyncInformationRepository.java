/**
 * 
 */
package org.georchestra.photosobliques.storage.repository.async;

import org.georchestra.photosobliques.storage.entity.async.AsyncInformationEntity;
import org.georchestra.photosobliques.storage.entity.async.AsyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author FNI18300
 *
 */
@Repository
public interface AsyncInformationRepository extends JpaRepository<AsyncInformationEntity, Long>, Serializable {

	AsyncInformationEntity findByUuid(UUID uuid);

	List<AsyncInformationEntity> findByStatusAndCreationDateLessThan(AsyncStatus status, Date date);

}
