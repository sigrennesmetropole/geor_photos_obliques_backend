package org.georchestra.photosobliques.storage.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.georchestra.photosobliques.core.bean.impl.AbstractStampedLabelizedPhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;
import org.georchestra.photosobliques.storage.entity.AbstractStampedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @param <E> type des entités manipulées
 * @param <C> type du critère de recherche
 */
public abstract class AbstractStampedCustomRepositoryImpl<E extends AbstractStampedEntity, C extends AbstractStampedLabelizedPhotosObliquesCriteria>
		extends AbstractCustomRepositoryImpl<E, C> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStampedCustomRepositoryImpl.class);

	protected AbstractStampedCustomRepositoryImpl(EntityManager entityManager, Class<E> entitiesClass) {
		super(entityManager, entitiesClass);
	}

	/**
	 * @param searchCriteria critere de recherche
	 * @param builder        builder
	 * @param criteriaQuery
	 * @param root           la racine de la requête
	 * @param predicates     les predicats
	 */
	@Override
	protected void addPredicates(C searchCriteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery,
			Root<E> root, List<Predicate> predicates) {
		// code
		predicateStringCriteria(searchCriteria.getCode(), RepositoryConstants.FIELD_CODE, true, predicates, builder,
				root);

		// label
		predicateStringCriteria(searchCriteria.getLabel(), RepositoryConstants.FIELD_LABEL, true, predicates, builder,
				root);

		// inactif
		if (Boolean.TRUE.equals(searchCriteria.getActive())) {
			final LocalDateTime d = LocalDateTime.now();
			predicates.add(builder.and(builder.lessThanOrEqualTo(root.get(RepositoryConstants.FIELD_OPENING_DATE), d),
					builder.or(builder.greaterThanOrEqualTo(root.get(RepositoryConstants.FIELD_CLOSING_DATE), d),
							builder.isNull(root.get(RepositoryConstants.FIELD_CLOSING_DATE)))));
		}
	}

	protected <T> T getSingleResult(CriteriaQuery<T> query, Selection<T> selection, Predicate predicate) {
		query.where(predicate);
		if (selection != null) {
			query.select(selection);
		}
		List<T> resultats = this.getEntityManager().createQuery(query).getResultList();
		if (resultats.isEmpty()) {
			return null;
		}
		if (resultats.size() > 1) {
			LOGGER.warn("SingleResult attendu, mais plusieurs résultats récupérés");
		}
		return resultats.get(0);
	}

	protected <T> PageImpl<T> getPagedResults(CriteriaQuery<T> query, Selection<T> selection, Predicate predicate,
			Pageable pageable, Long totalCount) {

		query.where(predicate);
		query.select(selection);

		TypedQuery<T> typedQuery = this.getEntityManager().createQuery(query);
		List<T> entities;

		if (pageable.isUnpaged()) {
			entities = typedQuery.getResultList();
		} else {
			entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
					.getResultList();
		}

		return new PageImpl<>(entities, pageable, totalCount.intValue());
	}
}
