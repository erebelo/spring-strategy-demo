package com.erebelo.springstrategydemo.service.impl;

import com.erebelo.springstrategydemo.exception.model.NotFoundException;
import com.erebelo.springstrategydemo.mapper.RelationshipMapper;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.erebelo.springstrategydemo.repository.MongoRepository;
import com.erebelo.springstrategydemo.service.RelationshipService;
import com.erebelo.springstrategydemo.service.adapter.RelationshipAdapter;
import com.erebelo.springstrategydemo.support.DeepObjectComparator;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class RelationshipServiceImpl implements RelationshipService {

    private final Map<RelationshipAdapterType, RelationshipAdapter> adapters;
    private final MongoRepository mongoRepository;
    private final RelationshipMapper relationshipMapper;
    private final ObjectMapper objectMapper;
    private final DeepObjectComparator comparator;

    public RelationshipServiceImpl(List<RelationshipAdapter> adapterList, MongoRepository mongoRepository,
            RelationshipMapper relationshipMapper, ObjectMapper objectMapper, DeepObjectComparator comparator) {
        this.adapters = adapterList.stream()
                .collect(Collectors.toMap(RelationshipAdapter::getAdapterType, Function.identity()));
        this.mongoRepository = mongoRepository;
        this.relationshipMapper = relationshipMapper;
        this.objectMapper = objectMapper;
        this.comparator = comparator;

        log.info("Initialized RelationshipServiceV2 with {} adapters. adapterTypes={}", adapters.size(),
                adapters.keySet());
    }

    @Transactional
    public <P extends RelationshipPropertiesRequest> RelationshipResponse upsertRelationship(
            RelationshipRequest<P> request) {
        RelationshipAdapterType adapterType = request.getAdapterType();
        log.info("[{}] Upserting relationship. from.identifier={}, to.identifier={}", adapterType,
                request.getFrom().getIdentifier(), request.getTo().getIdentifier());

        RelationshipAdapter adapter = getAdapter(adapterType);

        RelationshipNode fromNode = adapter.resolveFromNode(request.getFrom());
        RelationshipNode toNode = adapter.resolveToNode(request.getTo());
        RelationshipProperties properties = adapter.resolveRelationshipProperties(request.getProperties());

        Criteria criteria = adapter.baseIdentityCriteria(adapterType, request.getFrom(), request.getTo());
        adapter.customUpsertCriteria(request, criteria);

        Relationship existingRelationship = mongoRepository.findOneByCriteria(criteria, Relationship.class);

        Relationship relationship;

        if (existingRelationship != null) {
            log.info("[{}] Found existing relationship, updating with ID={}.", adapterType,
                    existingRelationship.getId());

            JsonNode original = comparator.toTypedTree(existingRelationship);

            relationship = existingRelationship;
            relationship.setFrom(fromNode);
            relationship.setTo(toNode);
            relationship.setProperties(properties);
            relationship.setStartDate(request.getStartDate());
            relationship.setEndDate(request.getEndDate());
            relationship.setAdapterType(request.getAdapterType());

            if (!comparator.deepEquals(original, comparator.toTypedTree(relationship))) {
                mongoRepository.save(relationship);

                log.info("[{}] Successfully updated relationship with ID={}.", adapterType, relationship.getId());
            } else {
                log.info("[{}] No changes detected for relationship with ID={}.", adapterType, relationship.getId());
            }
        } else {
            log.info("[{}] No existing relationship found, creating new one.", adapterType);

            relationship = Relationship.builder().from(fromNode).to(toNode).properties(properties)
                    .startDate(request.getStartDate()).endDate(request.getEndDate())
                    .adapterType(request.getAdapterType()).build();

            mongoRepository.insert(relationship);

            log.info("[{}] Successfully inserted relationship with ID={}.", adapterType, relationship.getId());
        }

        return relationshipMapper.toRelationshipResponse(relationship, objectMapper);
    }

    @Transactional
    public <P> RelationshipResponse expireRelationship(RelationshipExpireRequest<P> request) {
        RelationshipAdapterType adapterType = request.getAdapterType();
        log.info("[{}] Expiring relationship. from.identifier={}, to.identifier={}", adapterType,
                request.getFrom().getIdentifier(), request.getTo().getIdentifier());

        RelationshipAdapter adapter = getAdapter(adapterType);

        Criteria criteria = adapter.baseIdentityCriteria(adapterType, request.getFrom(), request.getTo());
        adapter.customExpireCriteria(request, criteria);

        Relationship relationship = mongoRepository.findOneByCriteria(criteria, Relationship.class);

        if (relationship == null) {
            throw new NotFoundException(
                    "[%s] No relationship found to expire by matching criteria.".formatted(adapterType));
        }

        adapter.enrichRelationshipProperties(relationship.getProperties());
        relationship.setEndDate(LocalDate.now(ZoneOffset.UTC));

        mongoRepository.save(relationship);
        adapter.enrichRelationshipNodeProperties(List.of(relationship));

        log.info("[{}] Successfully expired relationship with ID={}.", adapterType, relationship.getId());

        return relationshipMapper.toRelationshipResponse(relationship, objectMapper);
    }

    @Transactional
    public RelationshipResponse expireRelationshipById(String relationshipId, RelationshipAdapterType adapterType) {
        log.info("[{}] Expiring relationship by ID={}.", adapterType, relationshipId);

        RelationshipAdapter adapter = getAdapter(adapterType);

        Criteria criteria = adapter.baseByIdCriteria(adapterType, relationshipId);

        Relationship relationship = mongoRepository.findOneByCriteria(criteria, Relationship.class);

        if (relationship == null) {
            throw new NotFoundException(
                    "[%s] No active relationship found with ID=%s.".formatted(adapterType, relationshipId));
        }

        adapter.enrichRelationshipProperties(relationship.getProperties());
        relationship.setEndDate(LocalDate.now(ZoneOffset.UTC));

        mongoRepository.save(relationship);
        adapter.enrichRelationshipNodeProperties(List.of(relationship));

        log.info("[{}] Successfully expired relationship by ID={}.", adapterType, relationship.getId());

        return relationshipMapper.toRelationshipResponse(relationship, objectMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public <P extends RelationshipPropertiesSearchRequest> Page<@NonNull RelationshipResponse> searchRelationships(
            RelationshipSearchRequest<P> request, Pageable pageable) {
        RelationshipAdapterType adapterType = request.getAdapterType();
        log.info("[{}] Fetching relationships with search criteria.", adapterType);

        RelationshipAdapter adapter = getAdapter(adapterType);

        Criteria criteria = adapter.baseSearchCriteria(adapterType, request);
        adapter.customSearchCriteria(request, criteria);

        Page<@NonNull Relationship> relationshipPage = mongoRepository.findByCriteria(criteria, pageable,
                Relationship.class);
        List<Relationship> relationships = relationshipPage.getContent();

        if (!relationships.isEmpty()) {
            adapter.enrichRelationshipNodeProperties(relationships);
        }

        log.info("[{}] Found {} relationships.", adapterType, relationshipPage.getTotalElements());

        return relationshipPage
                .map(relationship -> relationshipMapper.toRelationshipResponse(relationship, objectMapper));
    }

    private RelationshipAdapter getAdapter(RelationshipAdapterType adapterType) {
        return Optional.ofNullable(adapters.get(adapterType)).orElseThrow(() -> new IllegalArgumentException(
                "Unknown adapter: %s. Available adapter types: %s.".formatted(adapterType, adapters.keySet())));
    }
}
