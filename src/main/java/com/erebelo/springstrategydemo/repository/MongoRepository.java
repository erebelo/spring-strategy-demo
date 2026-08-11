package com.erebelo.springstrategydemo.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
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

        if (!Arrays.asList(includeFields).contains("id")) {
            query.fields().exclude("_id");
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

        if (!Arrays.asList(includeFields).contains("id")) {
            query.fields().exclude("_id");
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
     * Find documents by custom criteria with pagination.
     *
     * @param criteria
     *            the criteria to match
     * @param pageable
     *            pagination information
     * @param entityClass
     *            the document class type
     * @param <T>
     *            the document type
     * @return a paginated result containing the matching documents
     */
    public <T> Page<@NonNull T> findByCriteria(Criteria criteria, Pageable pageable, Class<T> entityClass) {
        log.debug("Finding {} by custom criteria with pagination: page={}, size={}", entityClass.getSimpleName(),
                pageable.getPageNumber(), pageable.getPageSize());

        Query query = new Query(criteria).with(pageable);

        List<T> content = mongoTemplate.find(query, entityClass);

        // Count all matching documents ignoring pagination.
        // Query.with(pageable) adds skip/limit for the result page, but the total count
        // must represent the complete result set.
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), entityClass);

        Page<@NonNull T> result = PageableExecutionUtils.getPage(content, pageable, () -> total);

        log.debug("Found {} {}(s) out of {} total", content.size(), entityClass.getSimpleName(), total);

        return result;
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
