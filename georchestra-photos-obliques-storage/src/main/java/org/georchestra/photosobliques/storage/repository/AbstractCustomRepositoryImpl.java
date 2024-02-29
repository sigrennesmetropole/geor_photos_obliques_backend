package org.georchestra.photosobliques.storage.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.storage.common.RepositoryConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

	protected void predicateUuidCriteria(UUID uuid, String type, List<Predicate> predicates, CriteriaBuilder builder,
			Root<?> root) {
		if (uuid != null) {
			predicates.add(builder.equal(root.get(type), uuid));
		}
	}

	/**
	 *
	 * @param uuids      la liste de valeurs
	 * @param type       le type cible
	 * @param predicates les prédicats
	 * @param builder    le builder
	 * @param root       la racine
	 */
	protected void predicateUuidsCriteria(List<UUID> uuids, String type, List<Predicate> predicates,
			CriteriaBuilder builder, From<?, ?> root) {
		if (CollectionUtils.isNotEmpty(uuids)) {
			predicates.add(root.get(type).in(uuids));
		}
	}

	protected void predicateCriteriaNullOrNot(Boolean criteria, String type, List<Predicate> predicates,
			CriteriaBuilder builder, Root<?> root) {

		if (Boolean.TRUE.equals(criteria)) {
			predicates.add(builder.isNull(root.get(type)));
		} else {
			predicates.add(builder.isNotNull(root.get(type)));
		}

	}

	protected void predicateBooleanOrGreaterThanIntegerCriteria(Boolean criteria, Integer lowerValue,
			List<String> types, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
		if (Boolean.TRUE.equals(criteria)) {
			Predicate[] predicatesGreater = types.stream().map(type -> builder.greaterThan(root.get(type), lowerValue))
					.toArray(Predicate[]::new);
			predicates.add(builder.or(predicatesGreater));
		} else {
			Predicate[] predicatesLess = types.stream()
					.map(type -> builder.lessThanOrEqualTo(root.get(type), lowerValue)).toArray(Predicate[]::new);
			predicates.add(builder.and(predicatesLess));
		}
	}

	protected void predicateYearCriteria(Integer anneeDebut, Integer anneeFin, String type, List<Predicate> predicates,
			CriteriaBuilder builder, Root<?> root) {

		predicateBetweenCriteria(anneeDebut, anneeFin, type, predicates, builder, root);
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

	protected void predicateCodeAndLabel(String codeAndLibelle, List<Predicate> predicates, CriteriaBuilder builder,
			Root<?> root) {
		predicateCodeAndLabel(codeAndLibelle,
				Arrays.asList(RepositoryConstants.FIELD_CODE, RepositoryConstants.FIELD_LABEL), predicates, builder,
				root);
	}

	protected void predicateGeographicArea(String codeAndLibelle, List<Predicate> predicates, CriteriaBuilder builder,
			Root<?> root) {
		predicateGeographicArea(codeAndLibelle, RepositoryConstants.FIELD_CODE, RepositoryConstants.FIELD_LABEL,
				predicates, builder, root);
	}

	protected void predicateGeographicArea(String codeAndLibelle, String fieldNameCode, String fieldNameLabel,
			List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
		predicateGeographicArea(codeAndLibelle,
				StringUtils.isNotEmpty(fieldNameCode) ? Arrays.asList(fieldNameCode) : null,
				StringUtils.isNotEmpty(fieldNameLabel) ? Arrays.asList(fieldNameLabel) : null, predicates, builder,
				root);
	}

	/**
	 * Ajoute des prédicats de comparaison sur le champ code et libellé <br/>
	 * Cette méthode considère que le code est un numérique correspondant à un code
	 * insee
	 *
	 * @param codeAndLibelle Texte sur lequel faire la comparaison
	 * @param predicates     Liste dans laquelle on ajoute les prédicats
	 * @param builder        Le builder de la requête
	 * @param root           Le root de la requête
	 */
	protected void predicateGeographicArea(String codeAndLibelle, List<String> fieldNameCodes,
			List<String> fieldNameLabels, List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {

		if (StringUtils.isNotEmpty(codeAndLibelle)) {
			// on enlève ce qui n'est pas lettre et chiffre et espace et on remplace les
			// accents par les caractères non accentués
			String normalized = normalize(codeAndLibelle);
			// on coupe sur les espaces
			String[] parts = normalized.split(" ");
			List<String> codes = collectCodes(parts);

			predicateGeographicAreaCodes(codes, fieldNameCodes, predicates, builder, root);

			// on enlève tous les codes détectés comme insee
			normalized = removeCodes(normalized, codes);
			// on ajoute un critère ilike sur le libelle et on vérifie bien que ce n'est pas
			// qu'un espace sinon ça fait **
			if (StringUtils.isNotEmpty(normalized) && !normalized.equalsIgnoreCase(" ")) {
				// on utilise le reste commme du texte de libellé
				normalized = "*" + normalized.replace(" ", "*") + "*";
				if (CollectionUtils.isNotEmpty(fieldNameLabels)) {
					for (String fieldNameLabel : fieldNameLabels) {
						predicateStringCriteria(normalized, fieldNameLabel, true, predicates, builder, root);
					}
				}
			}
		}
	}

	private void predicateGeographicAreaCodes(List<String> codes, List<String> fieldNameCodes,
			List<Predicate> predicates, CriteriaBuilder builder, Root<?> root) {
		List<Predicate> predicateCodes = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(fieldNameCodes)) {
			for (String code : codes) {
				String value = code + "%";
				for (String fieldNameCode : fieldNameCodes) {
					predicateCodes.add(builder.like(root.get(fieldNameCode), value));
				}
			}
		}

		// on utilise tous les critères en ET
		if (CollectionUtils.isNotEmpty(predicateCodes)) {
			predicates.add(builder.or(predicateCodes.toArray(Predicate[]::new)));
		}
	}

	private List<String> collectCodes(String[] parts) {
		List<String> codes = new ArrayList<>();
		for (String part : parts) {
			// si c'est une sorte de code insee
			if (part.matches("\\d.*")) {
				codes.add(part);
			}
		}
		return codes;
	}

	private String removeCodes(String normalized, List<String> codes) {
		for (String code : codes) {
			normalized = normalized.replaceAll(code, "");
		}
		return normalized.trim();
	}

	protected void predicateUsers(String firstnameAndLastName, List<Predicate> predicates, CriteriaBuilder builder,
			Root<?> root) {
		if (StringUtils.isNotEmpty(firstnameAndLastName)) {
			String normalized = normalize(firstnameAndLastName).replace("-", " ");
			String[] parts = normalized.split(" ");
			List<Predicate> predicateName = new ArrayList<>();
			for (String part : parts) {
				String partTrim = "*" + part.trim() + "*";
				predicateName.add(buildPredicateStringCriteria(partTrim, RepositoryConstants.FIELD_FIRSTNAME, true,
						builder, root));
				predicateName.add(buildPredicateStringCriteria(partTrim, RepositoryConstants.FIELD_LASTNAME, true,
						builder, root));
			}
			if (CollectionUtils.isNotEmpty(predicateName)) {
				predicates.add(builder.or(predicateName.toArray(Predicate[]::new)));
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
				expression = builder.function("unaccent", String.class, builder.lower(root.get(type)));
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
