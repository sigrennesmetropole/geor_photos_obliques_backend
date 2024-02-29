package org.georchestra.photosobliques.storage.repository.historic.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.georchestra.photosobliques.core.bean.historic.HistoricDataPhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.historic.HistoricDataEntity;
import org.georchestra.photosobliques.storage.repository.AbstractCustomRepositoryImpl;
import org.georchestra.photosobliques.storage.repository.historic.HistoricDataCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class HistoricDataCustomRepositoryImpl
				extends AbstractCustomRepositoryImpl<HistoricDataEntity, HistoricDataPhotosObliquesCriteria>
				implements HistoricDataCustomRepository {

	private static final String FIELD_WHEN = "when";
	private static final String FIELD_WHICH_TYPE = "whichType";
	private static final String FIELD_WHO = "who";
	private static final String FIELD_WHICH_UUID = "which";

	public HistoricDataCustomRepositoryImpl(EntityManager entityManager) {
		super(entityManager, HistoricDataEntity.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<HistoricDataEntity> searchHistoricDatum(HistoricDataPhotosObliquesCriteria searchCriteria, Pageable pageable) {
		return search(searchCriteria, pageable);
	}

	@Override
	protected void addPredicates(HistoricDataPhotosObliquesCriteria searchCriteria, CriteriaBuilder builder,
                                 CriteriaQuery<?> criteriaQuery, Root<HistoricDataEntity> root, List<Predicate> predicates) {
		super.addPredicates(searchCriteria, builder, criteriaQuery, root, predicates);

		//Filtre sur le which UUID
		if (searchCriteria.getWhichUuid() != null) {
			predicateUuidCriteria(searchCriteria.getWhichUuid(), FIELD_WHICH_UUID, predicates, builder, root);
		}

		//Filtre sur le whichType
		if (searchCriteria.getWhichType() != null) {
			predicateStringCriteria(searchCriteria.getWhichType(), FIELD_WHICH_TYPE, false, predicates, builder, root);
		}

		//Filtre sur l'auteur de la modification
		if (searchCriteria.getWho() != null) {
			predicateStringCriteria(searchCriteria.getWho(), FIELD_WHO, false, predicates, builder, root);
		}

		//Filtre sur la date de la modification
		if (searchCriteria.getDateMin() != null && searchCriteria.getDateMax() != null) {
			predicateDateCriteria(searchCriteria.getDateMin(), searchCriteria.getDateMax(), FIELD_WHEN, predicates, builder, root);
		} else if (searchCriteria.getDateMin() != null) {
			predicateDateCriteriaGreaterThan(searchCriteria.getDateMin(), FIELD_WHEN, predicates, builder, root);
		} else if (searchCriteria.getDateMax() != null) {
			predicateDateCriteriaLessThan(searchCriteria.getDateMax(), FIELD_WHEN, predicates, builder, root);
		}
	}
}
