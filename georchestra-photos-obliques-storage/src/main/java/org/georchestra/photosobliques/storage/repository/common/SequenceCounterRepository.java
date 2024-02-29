package org.georchestra.photosobliques.storage.repository.common;

import jakarta.persistence.LockModeType;
import org.georchestra.photosobliques.storage.entity.common.SequenceCounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

/**
 * Repository pour les séquences
 *
 * @author FNI18300
 *
 */
@Repository
public interface SequenceCounterRepository extends JpaRepository<SequenceCounterEntity, Long> {

	/**
	 * Recherche un compteur en fonction de son code Si plusieurs lignes retournées
	 * une erreur est générée (Méthode spring data de type "find one")
	 *
	 * @param code Code du compteur
	 * @return Cf. description
	 */
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	SequenceCounterEntity findOneByCode(String code);

}
