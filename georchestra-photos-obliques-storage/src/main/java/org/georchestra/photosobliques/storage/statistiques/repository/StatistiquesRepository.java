/**
 *
 */
package org.georchestra.photosobliques.storage.statistiques.repository;

import org.georchestra.photosobliques.storage.statistiques.entity.StatistiquesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatistiquesRepository extends JpaRepository<StatistiquesEntity, Long> {
}
