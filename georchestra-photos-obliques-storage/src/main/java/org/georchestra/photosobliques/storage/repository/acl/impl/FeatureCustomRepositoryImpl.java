package org.georchestra.photosobliques.storage.repository.acl.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.georchestra.photosobliques.core.bean.acl.FeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.entity.acl.FeatureEntity;
import org.georchestra.photosobliques.storage.repository.AbstractCustomRepositoryImpl;
import org.georchestra.photosobliques.storage.repository.acl.FeatureCustomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository custom pour les Features d'utilisateurs
 *
 *
 */
@Repository
public class FeatureCustomRepositoryImpl extends AbstractCustomRepositoryImpl<FeatureEntity, FeaturePhotosObliquesCriteria> implements FeatureCustomRepository {

	// Champs utilisés pour le filtrage
	public static final String FIELD_CODE = "code";
	public static final String FIELD_LABEL = "label";
	public static final String FIELD_OPENING_DATE = "openingDate";
	public static final String FIELD_CLOSING_DATE = "closingDate";

	public FeatureCustomRepositoryImpl(EntityManager entityManager) {
		super(entityManager, FeatureEntity.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<FeatureEntity> searchFeatures(FeaturePhotosObliquesCriteria searchCriteria) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<FeatureEntity> searchQuery = builder.createQuery(FeatureEntity.class);
		Root<FeatureEntity> searchRoot = searchQuery.from(FeatureEntity.class);
		buildQuery(searchCriteria, builder, searchQuery, searchRoot);

		searchQuery.select(searchRoot);
		searchQuery.orderBy(QueryUtils.toOrders(Sort.by("order", FIELD_LABEL), searchRoot, builder));

		TypedQuery<FeatureEntity> typedQuery = getEntityManager().createQuery(searchQuery);
		return typedQuery.getResultList();
	}

	private void buildQuery(FeaturePhotosObliquesCriteria searchCriteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery,
                            Root<FeatureEntity> root) {

		if (searchCriteria != null) {
			List<Predicate> predicates = new ArrayList<>();

			// code
			predicateStringCriteria(searchCriteria.getCode(), FIELD_CODE, false, predicates, builder, root);

			// label
			predicateStringCriteria(searchCriteria.getLabel(), FIELD_LABEL, false, predicates, builder, root);

			// inactif
			if (Boolean.TRUE.equals(searchCriteria.getActive())) {
				final LocalDateTime d = LocalDateTime.now();
				predicates.add(builder.and(builder.lessThanOrEqualTo(root.get(FIELD_OPENING_DATE), d),
						builder.or(builder.greaterThanOrEqualTo(root.get(FIELD_CLOSING_DATE), d),
								builder.isNull(root.get(FIELD_CLOSING_DATE)))));
			}

			// Définition de la clause Where
			if (CollectionUtils.isNotEmpty(predicates)) {
				criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
			}

		}
	}

}
