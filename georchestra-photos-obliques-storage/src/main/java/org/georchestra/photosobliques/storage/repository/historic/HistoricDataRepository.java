/**
 * SHOM - Search
 */
package org.georchestra.photosobliques.storage.repository.historic;

import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author FNI18300
 *
 */
@Repository
public interface HistoricDataRepository extends JpaRepository<HistoricDataEntity, Long>, Serializable {

}
