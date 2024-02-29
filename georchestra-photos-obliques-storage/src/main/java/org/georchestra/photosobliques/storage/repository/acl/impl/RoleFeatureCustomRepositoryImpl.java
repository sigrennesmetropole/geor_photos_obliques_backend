package org.georchestra.photosobliques.storage.repository.acl.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.commons.collections4.CollectionUtils;
import org.georchestra.photosobliques.core.bean.acl.RoleFeaturePhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;
import org.georchestra.photosobliques.storage.entity.acl.FeatureScope;
import org.georchestra.photosobliques.storage.entity.acl.RoleEntity;
import org.georchestra.photosobliques.storage.entity.acl.RoleFeatureEntity;
import org.georchestra.photosobliques.storage.repository.AbstractCustomRepositoryImpl;
import org.georchestra.photosobliques.storage.repository.acl.RoleFeatureCustomRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao custom pour les roles de features
 */
@Repository
public class RoleFeatureCustomRepositoryImpl
		extends AbstractCustomRepositoryImpl<RoleFeatureEntity, RoleFeaturePhotosObliquesCriteria>
		implements RoleFeatureCustomRepository {

	private static final String FIELD_SCOPE = "scope";

	protected RoleFeatureCustomRepositoryImpl(EntityManager entityManager) {
		super(entityManager, RoleFeatureEntity.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<RoleFeatureEntity> searchRolesFeature(RoleFeaturePhotosObliquesCriteria searchCriteria) {

		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

		// Requête de recherche
		CriteriaQuery<RoleFeatureEntity> searchQuery = builder.createQuery(RoleFeatureEntity.class);
		Root<RoleFeatureEntity> searchRoot = searchQuery.from(RoleFeatureEntity.class);
		buildQuery(searchCriteria, builder, searchQuery, searchRoot);

		searchQuery.select(searchRoot);

		TypedQuery<RoleFeatureEntity> typedQuery = getEntityManager().createQuery(searchQuery);
		return typedQuery.getResultList();
	}

	private void buildQuery(RoleFeaturePhotosObliquesCriteria searchCriteria, CriteriaBuilder builder,
                            CriteriaQuery<?> criteriaQuery, Root<RoleFeatureEntity> root) {

		if (searchCriteria != null) {
			List<Predicate> predicates = new ArrayList<>();

			// scope
			predicateEnumCriteria(convertEnum(searchCriteria.getScope(), FeatureScope.class), FIELD_SCOPE, predicates,
					builder, root);

			if (searchCriteria.getRoleUuid() != null) {
				Subquery<Long> subRoleQuery = criteriaQuery.subquery(Long.class);
				Root<RoleEntity> subRole = subRoleQuery.from(RoleEntity.class);
				Join<RoleEntity, RoleFeatureEntity> joinRoleFeatures = subRole.join("roleFeatures", JoinType.INNER);
				subRoleQuery.where(
						builder.equal(subRole.get(RepositoryConstants.FIELD_UUID), searchCriteria.getRoleUuid()));
				subRoleQuery.select(joinRoleFeatures.get(RepositoryConstants.FIELD_ID));

				predicates.add(root.get(RepositoryConstants.FIELD_ID).in(subRoleQuery));
			}

			// Définition de la clause Where
			if (CollectionUtils.isNotEmpty(predicates)) {
				criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
			}

		}
	}
}
