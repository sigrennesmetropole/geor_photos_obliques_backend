package org.georchestra.photosobliques.storage.phototheque.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.storage.phototheque.entity.PhotoObliqueEntity;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <E> type des entités manipulées
 * @param <C> type du critère de recherche
 */
@RequiredArgsConstructor
public class AbstractCustomRepositoryImpl<E, C> {

	@Getter(value = AccessLevel.PROTECTED)
	private final EntityManager entityManager;

	@Getter(value = AccessLevel.PROTECTED)
	private final Class<E> entitiesClass;

	protected static final int SRID = 3948;

	protected static final String FONCTION_ST_AREA = "public.st_area";
	protected static final String FONCTION_GEOM_FEOM_TEXT = "public.st_geomfromtext";
	protected static final String FONCTION_ST_INTERSECTION = "public.st_intersection";
	protected static final String FONCTION_ST_INTERSECTS = "public.st_intersects";
	protected static final String FONCTION_GREATEST = "GREATEST";
	protected static final String FONCTION_ST_SET_SRID = "public.st_setSRID";

	protected Page<E> search(@Nullable C searchCriteria, Pageable pageable) {

		if (searchCriteria == null) {
			return emptyPage(pageable);
		}

		final Long totalCount = getTotalCount(entitiesClass, searchCriteria);
		if (totalCount == 0) {
			return emptyPage(pageable);
		}

		// Requête de recherche
		val builder = entityManager.getCriteriaBuilder();
		val searchQuery = builder.createQuery(entitiesClass);
		val searchRoot = searchQuery.from(entitiesClass);
		addWhere(searchCriteria, builder, searchQuery, searchRoot);
		searchQuery.select(searchRoot).distinct(true)
				.orderBy(QueryUtils.toOrders(pageable.getSort(), searchRoot, builder));

		val typedQuery = entityManager.createQuery(searchQuery);
		List<E> entities = null;
		if (pageable.isUnpaged()) {
			entities = typedQuery.getResultList();
		} else {
			entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
					.getResultList();
		}
		return new PageImpl<>(entities, pageable, totalCount.intValue());
	}

	protected Long getTotalCount(Class<E> entitiesClass, C searchCriteria) {
		val builder = entityManager.getCriteriaBuilder();

		val countQuery = builder.createQuery(Long.class);
		val countRoot = countQuery.from(entitiesClass);
		addWhere(searchCriteria, builder, countQuery, countRoot);
		countQuery.select(builder.countDistinct(countRoot));
		return entityManager.createQuery(countQuery).getSingleResult();
	}

	/**
	 * Ajout d'un prédicat sur la requ
	 *
	 * @param criteria
	 * @param type
	 * @param unaccent
	 * @param predicates
	 * @param builder
	 * @param root
	 */
	protected void predicateStringCriteria(String criteria, String type, boolean unaccent, List<Predicate> predicates,
			CriteriaBuilder builder, From<?, ?> root) {
		if (StringUtils.isNotEmpty(criteria)) {
			predicates.add(buildPredicateStringCriteria(criteria, type, unaccent, builder, root));
		}
	}


	protected void predicateAngleCriteria(Double angleRecherche, Double tolerance, String type, List<Predicate> predicates,
			CriteriaBuilder builder, From<?, ?> root) {
		Predicate angleInTolerencePredicate = builder.between(
				root.get(type),
				builder.literal(angleRecherche - tolerance),
				builder.literal(angleRecherche + tolerance)
		);
		Predicate angleLessThanTolerencePredicate = builder.and(
				builder.lessThanOrEqualTo(builder.literal(angleRecherche), builder.literal(tolerance)),
				builder.greaterThanOrEqualTo(root.get(type), builder.literal(360-tolerance+angleRecherche))
		);

		Predicate angleGreaterThanTolerencePredicate = builder.and(
				builder.greaterThanOrEqualTo(builder.literal(angleRecherche), builder.literal(360-tolerance)),
				builder.lessThanOrEqualTo(root.get(type), builder.literal(tolerance - 360 + angleRecherche))
		);


		predicates.add(builder.or(angleInTolerencePredicate, angleLessThanTolerencePredicate, angleGreaterThanTolerencePredicate));
	}

	protected void predicateEqualsCriteria(Integer integer, String type,
										   List<Predicate> predicates, CriteriaBuilder builder, From<?, ?> root) {
		predicates.add(
				builder.equal(
						builder.literal(integer),
						root.get(type)
				)
		);
	}

	protected void predicateStringCriteria(List<String> criterias, String type, boolean unaccent,
			List<Predicate> predicates, CriteriaBuilder builder, From<?, ?> root) {
		if (CollectionUtils.isNotEmpty(criterias)) {
			List<Predicate> predicateOrList = new ArrayList<>();
			for (String criteria : criterias) {
				Predicate predicate = buildPredicateStringCriteria(criteria, type, unaccent, builder, root);
				if (predicate != null) {
					predicateOrList.add(predicate);
				}
			}
			predicates.add(builder.or(predicateOrList.toArray(Predicate[]::new)));
		}
	}

	protected <T extends Enum<T>> void predicateEnumCriteria(T criteria, String type, List<Predicate> predicates,
			CriteriaBuilder builder, From<?, ?> root) {
		if (criteria != null) {
			predicates.add(builder.equal(root.get(type), criteria));
		}
	}

	/**
	 * Pour les listes d'énumérés
	 *
	 * @param <T>        le type d'énuméré
	 * @param criteria   le critère
	 * @param type       le type
	 * @param predicates la liste des prédicats
	 * @param builder    le builder
	 * @param root       l'objet racine
	 */
	protected <T extends Enum<T>> void predicateEnumCollectionCriteria(List<T> criteria, String type,
			List<Predicate> predicates, CriteriaBuilder builder, From<?, ?> root) {
		if (CollectionUtils.isNotEmpty(criteria)) {
			predicates.add(root.get(type).in(criteria));
		}
	}

	protected void predicateStringCriteriaForJoin(String criteria, String type, List<Predicate> predicates,
			CriteriaBuilder builder, Join<?, ?> join) {
		if (criteria != null) {
			if (criteria.indexOf('*') == -1) {
				predicates.add(builder.equal(join.get(type), criteria));
			} else {
				predicates.add(builder.like(join.get(type), criteria.replace("*", "%")));
			}
		}
	}

	protected void predicateStringCollectionCriteria(List<String> criteria, String type, List<Predicate> predicates,
			CriteriaBuilder builder, From<?, ?> root) {
		if (criteria != null) {
			predicates.add(builder.and(root.join(type).in(criteria)));
		}
	}

	protected void predicateDateCriteriaGreaterThan(LocalDateTime date, String type, List<Predicate> predicates,
			CriteriaBuilder builder, Root<?> root) {
		if (date != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(type), date));
		}
	}

	protected void predicateDateCriteriaLessThan(LocalDateTime date, String type, List<Predicate> predicates,
			CriteriaBuilder builder, Root<?> root) {
		if (date != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(type), date));
		}
	}

	protected void predicateDateCriteria(LocalDateTime dateDebut, LocalDateTime dateFin, String type,
			List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

		predicateBetweenCriteria(dateDebut, dateFin, type, predicates, builder, root);
	}

	protected void predicateBooleanCriteria(Boolean criteria, String type, List<Predicate> predicates,
			CriteriaBuilder builder, From<?, ?> root) {

		if (Boolean.TRUE.equals(criteria)) {
			predicates.add(builder.isTrue(root.get(type)));
		} else {
			predicates.add(builder.isFalse(root.get(type)));
		}

	}


	protected void predicateLessThanCriteria(Integer end, String type, List<Predicate> predicates,
											 CriteriaBuilder builder, Root<?> root) {

		predicateBetweenCriteria(null, end, type, predicates, builder, root);
	}

	protected void predicateGreaterThanCriteria(Integer start, String type, List<Predicate> predicates,
												CriteriaBuilder builder, Root<?> root) {

		predicateBetweenCriteria(start, null, type, predicates, builder, root);
	}

	protected void predicateIntersectGeometries(String geometryWKT, String type, List<Predicate> predicates, CriteriaBuilder builder, Root<PhotoObliqueEntity> root) {

		Expression<Geometry> searchGeometry = builder.function(
				FONCTION_GEOM_FEOM_TEXT,
				Geometry.class,
				builder.literal(geometryWKT).as(String.class),
				builder.literal(SRID)
		);
		Expression<Geometry> photoGeometry = builder.function(
				FONCTION_ST_SET_SRID,
				Geometry.class,
				root.get(type),
				builder.literal(SRID)
		);
		Expression<Geometry> searchGeometry2 = builder.function(
				FONCTION_ST_SET_SRID,
				Geometry.class,
				searchGeometry,
				builder.literal(SRID)
		);

		Expression<Boolean> expression = builder.function(
				FONCTION_ST_INTERSECTS,
				Boolean.class,
				photoGeometry,
				searchGeometry2
		);
		predicates.add(builder.isTrue(expression));

	}

	protected void predicateCodeAndLabel(String codeAndLibelle, List<String> fields, List<Predicate> predicates,
			CriteriaBuilder builder, Root<?> root) {
		if (StringUtils.isNotEmpty(codeAndLibelle)) {
			// on enlève ce qui n'est pas lettre et chiffre et espace et on remplace les
			// accents par les caractères non accentués
			String normalized = "*" + normalize(codeAndLibelle.replace('*', '%')) + "*";

			List<Predicate> predicatesOr = new ArrayList<>();
			for (String field : fields) {
				predicatesOr.add(buildPredicateStringCriteria(normalized, field, true, builder, root));
			}

			if (CollectionUtils.isNotEmpty(predicatesOr)) {
				predicates.add(builder.or(predicatesOr.toArray(Predicate[]::new)));
			}
		}
	}



	protected Predicate buildPredicateStringCriteria(String criteria, String type, boolean unaccent,
			CriteriaBuilder builder, From<?, ?> root) {
		if (StringUtils.isNotEmpty(criteria)) {
			boolean like = false;
			Expression<String> expression = null;
			if (criteria.indexOf('*') != -1) {
				like = true;
				criteria = criteria.replace("*", "%");
			}

			if (unaccent) {
				criteria = normalize(criteria).toLowerCase();
				expression = builder.function("public.unaccent", String.class, builder.lower(root.get(type)));
			} else {
				expression = root.get(type);
			}

			if (like) {
				return builder.like(expression, criteria);
			} else {
				return builder.equal(expression, criteria);
			}
		}
		return null;
	}

	@Nonnull
	protected PageImpl<E> emptyPage(Pageable pageable) {
		return new PageImpl<>(new ArrayList<>(), pageable, 0);
	}

	protected void addWhere(C searchCriteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery, Root<E> root) {

		if (searchCriteria != null) {
			List<Predicate> predicates = new ArrayList<>();

			addPredicates(searchCriteria, builder, criteriaQuery, root, predicates);

			// Définition de la clause Where
			if (CollectionUtils.isNotEmpty(predicates)) {
				criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
			}
		}
	}

	/**
	 * @param searchCriteria critere de recherche
	 * @param builder        builder
	 * @param criteriaQuery
	 * @param root           la racine de la requête
	 * @param predicates     les predicats
	 */
	protected void addPredicates(C searchCriteria, CriteriaBuilder builder, CriteriaQuery<?> criteriaQuery,
			Root<E> root, List<Predicate> predicates) {
		// Par défaut, aucun critère de recherche n'est imposé
	}

	private String normalize(String criteria) {
		String normalized = StringUtils.stripAccents(criteria.trim()).replaceAll("[^-a-zA-Z0-9% ]", "");
		normalized = StringUtils.normalizeSpace(normalized);
		return normalized;
	}

	private <T extends Comparable<? super T>> void predicateBetweenCriteria(T lower, T upper, String type,
			List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

		if (lower != null && upper != null) {
			predicates.add(builder.between(root.get(type), lower, upper));
		} else if (lower != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(type), lower));
		} else if (upper != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get(type), upper));
		}
	}

	protected <B extends Enum<B>, D extends Enum<D>> D convertEnum(B value, Class<D> clazz) {
		D result = null;
		if (value != null) {
			for (D item : clazz.getEnumConstants()) {
				if (item.name().equals(value.name())) {
					result = item;
					break;
				}
			}
		}
		return result;
	}

	protected <B extends Enum<B>, D extends Enum<D>> List<D> convertEnums(List<B> values, Class<D> clazz) {
		List<D> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(values)) {
			for (B value : values) {
				D item = convertEnum(value, clazz);
				if (item != null) {
					result.add(item);
				}
			}
		}
		return result;
	}
}
