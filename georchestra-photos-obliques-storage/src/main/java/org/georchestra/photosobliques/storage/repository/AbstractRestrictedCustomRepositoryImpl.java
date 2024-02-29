package org.georchestra.photosobliques.storage.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.georchestra.photosobliques.core.bean.RestrictedPhotosObliquesCriteria;
import org.georchestra.photosobliques.core.common.Restrictable;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <E> type des entités manipulées
 * @param <C> type du critère de recherche
 */
public class AbstractRestrictedCustomRepositoryImpl<E extends Restrictable, C extends RestrictedPhotosObliquesCriteria>
		extends AbstractCustomRepositoryImpl<E, C> {

	public AbstractRestrictedCustomRepositoryImpl(EntityManager entityManager, Class<E> entitiesClass) {
		super(entityManager, entitiesClass);
	}

	@Override
	protected void addWhere(C searchCriteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery, Root<E> root) {

		if (searchCriteria != null) {
			List<Predicate> predicates = new ArrayList<>();

			addPredicates(searchCriteria, builder, criteriaQuery, root, predicates);

			addRestrictedPredicate(searchCriteria, builder, criteriaQuery, root, predicates);

			// Définition de la clause Where
			if (CollectionUtils.isNotEmpty(predicates)) {
				criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
			}
		}
	}

	/**
	 * Ajoute si nécessaire un prédicat sur la restriction des accès
	 *
	 * @param searchCriteria le critère de recherche
	 * @param builder        le builder
	 * @param criteriaQuery  la query
	 * @param root           l'objet racine
	 * @param predicates     la liste des prédicats
	 */
	protected void addRestrictedPredicate(C searchCriteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery,
			Root<E> root, List<Predicate> predicates) {

		if (!searchCriteria.isRestricted()) {
			predicates.add(builder.equal(root.get(RepositoryConstants.FIELD_RESTRICTED), false));
		}

	}

}
