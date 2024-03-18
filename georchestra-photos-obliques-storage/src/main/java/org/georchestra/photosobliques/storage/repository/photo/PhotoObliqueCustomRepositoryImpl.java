package org.georchestra.photosobliques.storage.repository.photo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.photosobliques.core.bean.photo.PhotoObliqueSearchCriteria;
import org.georchestra.photosobliques.storage.entity.PhotoObliqueEntity;
import org.georchestra.photosobliques.storage.repository.AbstractCustomRepositoryImpl;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao custom pour les roles de features
 */
@Repository
public class PhotoObliqueCustomRepositoryImpl
        extends AbstractCustomRepositoryImpl<PhotoObliqueEntity, PhotoObliqueSearchCriteria>
        implements PhotoObliqueCustomRepository {


    private static final String FIELD_YEAR = "year";
    private static final String FIELD_SHAPE = "shape";
    private static final String FIELD_OWNER = "owner";
    private static final String FIELD_PROVIDER = "provider";
    private static final String FIELD_ANGLE = "angleDegre";
    private static final String FIELD_DOWNLOADABLE = "downloadable";
    private static final String FIELD_RELEVANCE = "relevance";

    protected PhotoObliqueCustomRepositoryImpl(EntityManager entityManager) {
        super(entityManager, PhotoObliqueEntity.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Tuple> searchPhotosObliquesWithRelevance(PhotoObliqueSearchCriteria searchCriteria, Double tolerence, Pageable pageable) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Tuple> query = builder.createTupleQuery();

        Root<PhotoObliqueEntity> searchRoot = query.from(PhotoObliqueEntity.class);

        buildQueryFromSeachCriteria(searchCriteria, tolerence, builder, query, searchRoot);

        // On sélectionne les Photos et la pertinance pour chaque résultat
        query.multiselect(searchRoot,
                predicateRelevance(searchCriteria.getGeometry(), builder, searchRoot)
        );

        // génération de la liste de trie
        List<Order> orders = toOrders(pageable.getSort(), searchRoot, builder, searchCriteria);
        query.distinct(true).orderBy(orders);

        TypedQuery<Tuple> typedQuery = getEntityManager().createQuery(query);

        List<Tuple> entities;
        if (pageable.isUnpaged()) {
            entities = typedQuery.getResultList();
        } else {
            entities = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
        return entities;
    }

    @Override
    public List<String> searchOwners(String geometry) {

        return getAvailableValuesFromGeometryAndField(geometry, FIELD_OWNER);
    }

    @Override
    public List<String> searchProviders(String geometry) {

        return getAvailableValuesFromGeometryAndField(geometry, FIELD_PROVIDER);
    }

    private List<String> getAvailableValuesFromGeometryAndField(String geometry, String fieldProvider) {
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<String> query = builder.createQuery(String.class);

        Root<PhotoObliqueEntity> searchRoot = query.from(PhotoObliqueEntity.class);

        buildQueryFromGeometry(geometry, builder, query, searchRoot);

        query.groupBy(searchRoot.get(fieldProvider)).select(searchRoot.get(fieldProvider));

        TypedQuery<String> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getResultList();
    }


    @Override
    public List<Integer> searchYears(String geometry) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);

        Root<PhotoObliqueEntity> searchRoot = query.from(PhotoObliqueEntity.class);

        buildQueryFromGeometry(geometry, builder, query, searchRoot);

        query.groupBy(searchRoot.get(FIELD_YEAR)).select(searchRoot.get(FIELD_YEAR));

        TypedQuery<Integer> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getResultList();
    }

    private List<Order> toOrders(Sort sort, Root<PhotoObliqueEntity> searchRoot, CriteriaBuilder builder, PhotoObliqueSearchCriteria searchCriteria) {
        List<Order> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            if(order.getProperty().equals(FIELD_RELEVANCE)) {
                orders.add(buildRelevanceOrder(order, searchRoot, builder, searchCriteria));
            } else {
                addPhotoOrders(searchRoot, builder, orders, order);
            }
        }
        return orders;
    }

    private void addPhotoOrders(Root<PhotoObliqueEntity> searchRoot, CriteriaBuilder builder, List<Order> orders, Sort.Order order) {
        if(order.isIgnoreCase()) {
            Expression<String> expression = builder.lower(searchRoot.get(order.getProperty()));
            if (order.isAscending()) {
                orders.add(builder.asc(expression));
            } else {
                orders.add(builder.desc(expression));
            }
        } else {
            if (order.isAscending()) {
                orders.add(builder.asc(searchRoot.get(order.getProperty())));
            } else {
                orders.add(builder.desc(searchRoot.get(order.getProperty())));
            }
        }
    }

    private Order buildRelevanceOrder(Sort.Order order, Root<PhotoObliqueEntity> searchRoot, CriteriaBuilder builder, PhotoObliqueSearchCriteria searchCriteria) {

        if(order.isAscending()) {
            return builder.asc(predicateRelevance(searchCriteria.getGeometry(), builder, searchRoot));
        } else {
            return  builder.desc(predicateRelevance(searchCriteria.getGeometry(), builder, searchRoot));
        }
    }

    @Override
    public Integer countPhotosObliques(PhotoObliqueSearchCriteria searchCriteria, Double tolerence) {

        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Integer> query = builder.createQuery(Integer.class);

        Root<PhotoObliqueEntity> searchRoot = query.from(PhotoObliqueEntity.class);

        buildQueryFromSeachCriteria(searchCriteria, tolerence, builder, query, searchRoot);

        query.select(builder.count(searchRoot).as(Integer.class));

        TypedQuery<Integer> typedQuery = getEntityManager().createQuery(query);

        return typedQuery.getSingleResult();
    }

    /*
        Construction de la requête à partir du searchCriteria
     */
    private void buildQueryFromSeachCriteria(PhotoObliqueSearchCriteria searchCriteria, Double tolerence, CriteriaBuilder builder,
                                             CriteriaQuery<?> criteriaQuery, Root<PhotoObliqueEntity> root) {

        if (searchCriteria != null) {
            List<Predicate> predicates = new ArrayList<>();

            //on s'assure que l'on récupère les images téléchargeables uniquement
            predicateEqualsCriteria(1, FIELD_DOWNLOADABLE, predicates, builder, root);

            if (searchCriteria.getGeometry() != null) {
                predicateIntersectGeometries(searchCriteria.getGeometry(), FIELD_SHAPE, predicates, builder, root);
            }

            if (searchCriteria.getStartDate() != null) {
                predicateGreaterThanCriteria(searchCriteria.getStartDate(), FIELD_YEAR, predicates, builder, root);
            }

            if (searchCriteria.getEndDate() != null) {
                predicateLessThanCriteria(searchCriteria.getEndDate(), FIELD_YEAR, predicates, builder, root);
            }

            if (searchCriteria.getOwner() != null) {

                predicateStringCriteria(searchCriteria.getOwner(), FIELD_OWNER, true, predicates, builder, root);
            }

            if (searchCriteria.getProvider() != null) {
                predicateStringCriteria(searchCriteria.getProvider(), FIELD_PROVIDER, true, predicates, builder, root);
            }

            if(searchCriteria.getAngleDegre() != null) {
                predicateAngleCriteria(searchCriteria.getAngleDegre(), tolerence, FIELD_ANGLE, predicates, builder, root);
            }

            if (CollectionUtils.isNotEmpty(predicates)) {
                criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
            }

        }
    }

    /*
        Construction de la requête à partir d'une géométrie
     */
    private void buildQueryFromGeometry(String geometry, CriteriaBuilder builder,
                                             CriteriaQuery<?> criteriaQuery, Root<PhotoObliqueEntity> root) {

        List<Predicate> predicates = new ArrayList<>();

        //on s'assure que l'on récupère les images téléchargeables uniquement
        predicateEqualsCriteria(1, FIELD_DOWNLOADABLE, predicates, builder, root);

        if (StringUtils.isNotEmpty(geometry)) {
            predicateIntersectGeometries(geometry, FIELD_SHAPE, predicates, builder, root);
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            criteriaQuery.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
    }


    private Expression<Number> predicateRelevance(String geometry, CriteriaBuilder builder, Root<PhotoObliqueEntity> root) {

        Expression<Geometry> screenGeometryExpression = builder.function(
                FONCTION_GEOM_FEOM_TEXT,
                Geometry.class,
                builder.literal(geometry).as(String.class),
                builder.literal(3948)
        );


        Expression<Double> intersectionArea = builder.function(FONCTION_ST_AREA,
                Double.class,
                builder.function(FONCTION_ST_INTERSECTION, Double.class,  screenGeometryExpression, root.get(FIELD_SHAPE))
        );

        Expression<Double> greatestArea = builder.function(FONCTION_GREATEST,
                Double.class,
                builder.function(FONCTION_ST_AREA, Double.class, root.get(FIELD_SHAPE).as(String.class)),
                builder.function(FONCTION_ST_AREA, Double.class, screenGeometryExpression));


        return builder.prod(builder.literal(100.0),
                builder.quot(intersectionArea, greatestArea));
    }
}
