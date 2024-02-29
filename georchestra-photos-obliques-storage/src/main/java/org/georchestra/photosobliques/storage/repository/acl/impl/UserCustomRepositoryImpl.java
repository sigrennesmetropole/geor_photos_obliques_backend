package org.georchestra.photosobliques.storage.repository.acl.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.acl.UserPhotosObliquesCriteria;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;
import org.georchestra.photosobliques.storage.entity.acl.UserEntity;
import org.georchestra.photosobliques.storage.entity.acl.UserType;
import org.georchestra.photosobliques.storage.repository.AbstractCustomRepositoryImpl;
import org.georchestra.photosobliques.storage.repository.acl.UserCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository custom pour les users
 */
@Repository
public class UserCustomRepositoryImpl extends AbstractCustomRepositoryImpl<UserEntity, UserPhotosObliquesCriteria>
		implements UserCustomRepository {

	public UserCustomRepositoryImpl(EntityManager entityManager) {
		super(entityManager, UserEntity.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<UserEntity> searchUsers(UserPhotosObliquesCriteria searchCriteria, Pageable pageable) {
		return search(searchCriteria, pageable);
	}

	/**
	 * @param searchCriteria critere de recherche
	 * @param builder        builder
	 * @param criteriaQuery
	 * @param root           la racine de la requête
	 * @param predicates     les predicats
	 */
	@Override
	protected void addPredicates(UserPhotosObliquesCriteria searchCriteria, CriteriaBuilder builder,
                                 CriteriaQuery<?> criteriaQuery, Root<UserEntity> root, List<Predicate> predicates) {

		if (StringUtils.isNotEmpty(searchCriteria.getLogin())) {
			predicateStringCriteria(searchCriteria.getLogin(), RepositoryConstants.FIELD_LOGIN, false, predicates,
					builder, root);
		}

		if (StringUtils.isNotEmpty(searchCriteria.getFirstname())) {
			predicateStringCriteria(searchCriteria.getFirstname(), RepositoryConstants.FIELD_FIRSTNAME, true,
					predicates, builder, root);
		}

		if (StringUtils.isNotEmpty(searchCriteria.getLastname())) {
			predicateStringCriteria(searchCriteria.getLastname(), RepositoryConstants.FIELD_LASTNAME, true, predicates,
					builder, root);
		}

		if (StringUtils.isNotEmpty(searchCriteria.getFullname())) {
			predicateUsers(searchCriteria.getFullname(), predicates, builder, root);
		}

		// type
		if (searchCriteria.getType() != null) {
			predicates.add(builder.equal(root.get(RepositoryConstants.FIELD_TYPE),
					UserType.valueOf(searchCriteria.getType().name())));
		}

	}

}
