package org.georchestra.photosobliques.storage.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;
import org.georchestra.photosobliques.storage.entity.AbstractLongIdEntity;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation par défaut pour les entités "stamped" permettant de filtrer
 * celles-ci à une date donnée en fonction de leur activation
 *
 * @author FNI18300
 *
 */
@SuppressWarnings({ "java:S2055" }) // Impossible d'ajouter un constructeur sans argument dans Spring Data
public class StampedRepositoryImpl<T extends AbstractLongIdEntity> extends SimpleJpaRepository<T, Long>
		implements StampedRepository<T> {

	private static final long serialVersionUID = 2670116844084458196L;

	private transient EntityManager entityManager;

	// le type courant de l'instantiation
	private final Class<T> type;

	/**
	 * Le constructeur par défaut nécessaire à JPA
	 *
	 * @param entityInformation
	 * @param entityManager
	 */
	public StampedRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		type = entityInformation.getJavaType();
		this.entityManager = entityManager;
	}

	public List<T> findActive(LocalDateTime d) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> searchQuery = builder.createQuery(type);
		Root<T> searchRoot = searchQuery.from(type);

		buildQuery(d, builder, searchQuery, searchRoot);
		applySortCriteria(builder, searchQuery, searchRoot);

		searchQuery.distinct(true);
		TypedQuery<T> typedQuery = entityManager.createQuery(searchQuery);
		return typedQuery.getResultList();
	}

	private void applySortCriteria(CriteriaBuilder builder, CriteriaQuery<T> criteriaQuery, Root<T> searchRoot) {
		List<Order> orders = new ArrayList<>();
		orders.add(builder.asc(searchRoot.get(RepositoryConstants.FIELD_CODE)));
		orders.add(builder.asc(searchRoot.get(RepositoryConstants.FIELD_ID)));
		criteriaQuery.orderBy(orders);
	}

	private void buildQuery(LocalDateTime d, CriteriaBuilder builder, CriteriaQuery<T> criteriaQuery,
			Root<T> searchRoot) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.lessThanOrEqualTo(searchRoot.get(RepositoryConstants.FIELD_OPENING_DATE), d));
		predicates
				.add(builder.or(builder.greaterThanOrEqualTo(searchRoot.get(RepositoryConstants.FIELD_CLOSING_DATE), d),
						builder.isNull(searchRoot.get(RepositoryConstants.FIELD_CLOSING_DATE))));

		criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
	}

	@Override
	public T findByUuid(UUID uuid) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> searchQuery = builder.createQuery(type);
		Root<T> searchRoot = searchQuery.from(type);
		searchQuery.where(builder.equal(searchRoot.get("uuid"), uuid));

		return getFirstResult(searchQuery);
	}

	@Override
	public List<T> findByCode(String code, Boolean active) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> searchQuery = builder.createQuery(type);
		Root<T> searchRoot = searchQuery.from(type);

		buildQuery(code, active, builder, searchQuery, searchRoot);

		applySortCriteria(builder, searchQuery, searchRoot);

		TypedQuery<T> typedQuery = entityManager.createQuery(searchQuery);
		return typedQuery.getResultList();

	}

	private void buildQuery(String code, Boolean active, CriteriaBuilder builder, CriteriaQuery<T> searchQuery,
			Root<T> searchRoot) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(searchRoot.get(RepositoryConstants.FIELD_CODE), code));

		if (Boolean.TRUE.equals(active)) {
			LocalDateTime d = LocalDateTime.now();
			predicates.add(builder.lessThanOrEqualTo(searchRoot.get(RepositoryConstants.FIELD_OPENING_DATE), d));
			predicates.add(
					builder.or(builder.greaterThanOrEqualTo(searchRoot.get(RepositoryConstants.FIELD_CLOSING_DATE), d),
							builder.isNull(searchRoot.get(RepositoryConstants.FIELD_CLOSING_DATE))));
		}
		searchQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
	}

	@Override
	public T findFirstByCode(String code, Boolean active) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<T> searchQuery = builder.createQuery(type);
		Root<T> searchRoot = searchQuery.from(type);

		buildQuery(code, active, builder, searchQuery, searchRoot);

		applySortCriteria(builder, searchQuery, searchRoot);

		return getFirstResult(searchQuery);
	}

	private T getFirstResult(CriteriaQuery<T> searchQuery) {
		TypedQuery<T> typedQuery = entityManager.createQuery(searchQuery);
		typedQuery.setMaxResults(1);
		List<T> items = typedQuery.getResultList();
		if (items.isEmpty()) {
			return null;
		} else {
			return items.get(0);
		}
	}
}
