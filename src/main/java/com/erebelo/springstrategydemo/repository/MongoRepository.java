package com.erebelo.springstrategydemo.repository;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.FacetOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Generic MongoDB repository using MongoTemplate.
 * <p>
 * Provides dynamic query and persistence operations for any document type.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoRepository {

    private final MongoTemplate mongoTemplate;

    /**
     * Find a single document by a field value.
     *
     * @param fieldName
     *            the field name to match
     * @param value
     *            the field value to match
     * @param entityClass
     *            the document class type
     * @param includeFields
     *            optional fields to include in the result
     * @param <T>
     *            the document type
     * @return the found document or null if not found
     */
    public <T> T findOneByField(String fieldName, Object value, Class<T> entityClass, String... includeFields) {
        log.debug("Finding {} by {}={} with projections", entityClass.getSimpleName(), fieldName, value);

        Query query = new Query(Criteria.where(fieldName).is(value));
        for (String field : includeFields) {
            query.fields().include(field);
        }

        T result = mongoTemplate.findOne(query, entityClass);

        log.debug("Found {} result for {}={}: {}", entityClass.getSimpleName(), fieldName, value,
                result != null ? "yes" : "no");

        return result;
    }

    /**
     * Find all documents where the given field matches any of the provided values.
     *
     * @param fieldName
     *            the field name to match
     * @param values
     *            the field values to match
     * @param entityClass
     *            the document class type
     * @param includeFields
     *            optional fields to include in the result
     * @param <T>
     *            the document type
     * @return the matching documents, or an empty list if none are found
     */
    public <T> List<T> findAllByField(String fieldName, Collection<?> values, Class<T> entityClass,
            String... includeFields) {

        log.debug("Finding all {} by {} in {} with projections", entityClass.getSimpleName(), fieldName, values);

        Query query = new Query(Criteria.where(fieldName).in(values));

        for (String field : includeFields) {
            query.fields().include(field);
        }

        List<T> results = mongoTemplate.find(query, entityClass);

        log.debug("Found {} {}(s) for {} in {}", results.size(), entityClass.getSimpleName(), fieldName, values);

        return results;
    }

    /**
     * Find a single document by multiple field criteria.
     *
     * @param criteria
     *            the criteria to match
     * @param entityClass
     *            the document class type
     * @param <T>
     *            the document type
     * @return the found document or null if not found
     */
    public <T> T findOneByCriteria(Criteria criteria, Class<T> entityClass) {
        log.debug("Finding {} by custom criteria", entityClass.getSimpleName());

        Query query = new Query(criteria);
        T result = mongoTemplate.findOne(query, entityClass);

        log.debug("Found {} result with custom criteria: {}", entityClass.getSimpleName(),
                result != null ? "yes" : "no");

        return result;
    }

    /**
     * Find multiple documents by custom criteria.
     *
     * @param criteria
     *            the criteria to match
     * @param entityClass
     *            the document class type
     * @param includeFields
     *            optional fields to include in the result
     * @param <T>
     *            the document type
     * @return the matching documents
     */
    public <T> List<T> findByCriteria(Criteria criteria, Class<T> entityClass, String... includeFields) {
        log.debug("Finding multiple {} by custom criteria with projections", entityClass.getSimpleName());

        Query query = new Query(criteria);
        for (String field : includeFields) {
            query.fields().include(field);
        }

        List<T> results = mongoTemplate.find(query, entityClass);

        log.debug("Found {} results for {} with custom criteria", results.size(), entityClass.getSimpleName());

        return results;
    }

    /**
     * Find documents by multiple field criteria with pagination.
     *
     * @param criteria
     *            the criteria to match
     * @param pageable
     *            the pagination information
     * @param entityClass
     *            the document class type
     * @param <T>
     *            the document type
     * @return paginated results
     */
    public <T> Page<@NonNull T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> entityClass) {
        log.debug("Finding {} by criteria with pagination", entityClass.getSimpleName());

        String collectionName = mongoTemplate.getCollectionName(entityClass);

        MatchOperation matchStage = Aggregation.match(criteria);
        FacetOperation facetStage = Aggregation
                .facet(Aggregation.skip(pageable.getOffset()), Aggregation.limit(pageable.getPageSize())).as("data")
                .and(Aggregation.count().as("count")).as("totalCount");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, facetStage);

        List<Document> results = mongoTemplate.aggregate(aggregation, collectionName, Document.class)
                .getMappedResults();

        if (results.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        Document result = results.getFirst();
        List<Document> dataList = result.getList("data", Document.class);
        List<Document> countList = result.getList("totalCount", Document.class);

        long total = (countList != null && !countList.isEmpty()) ? countList.getFirst().getInteger("count", 0) : 0;

        List<T> entities = dataList.stream().map(doc -> mongoTemplate.getConverter().read(entityClass, doc)).toList();

        log.debug("Found {} total, returning page of {} for {}", total, entities.size(), entityClass.getSimpleName());

        return new PageImpl<>(entities, pageable, total);
    }

    /**
     * Save or update a document.
     *
     * @param document
     *            the document to save
     * @param <T>
     *            the document type
     */
    public <T> void save(T document) {
        log.debug("Saving document of type: {}", document.getClass().getSimpleName());

        mongoTemplate.save(document);

        log.debug("Successfully saved document of type: {}", document.getClass().getSimpleName());

    }

    /**
     * Insert a new document.
     *
     * @param document
     *            the document to insert
     * @param <T>
     *            the document type
     */
    public <T> void insert(T document) {
        log.debug("Inserting document of type: {}", document.getClass().getSimpleName());

        mongoTemplate.insert(document);

        log.debug("Successfully inserted document of type: {}", document.getClass().getSimpleName());

    }
}
