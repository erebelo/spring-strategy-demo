package com.erebelo.springstrategydemo.service.impl;

import com.erebelo.springstrategydemo.exception.model.NotFoundException;
import com.erebelo.springstrategydemo.mapper.RelationshipMapper;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class RelationshipServiceImpl implements RelationshipService {

    private final Map<RelationshipDataSource, RelationshipAdapter> adapters;
    private final MongoRepository mongoRepository;
    private final RelationshipMapper relationshipMapper;
    private final ObjectMapper objectMapper;
    private final DeepObjectComparator deepObjectComparator;

    public RelationshipServiceImpl(List<RelationshipAdapter> adapterList, MongoRepository mongoRepository,
            RelationshipMapper relationshipMapper, ObjectMapper objectMapper,
            DeepObjectComparator deepObjectComparator) {
        this.adapters = adapterList.stream()
                .collect(Collectors.toMap(RelationshipAdapter::getAdapterName, Function.identity()));
        this.mongoRepository = mongoRepository;
        this.relationshipMapper = relationshipMapper;
        this.objectMapper = objectMapper;
        this.deepObjectComparator = deepObjectComparator;

        log.info("Initialized RelationshipServiceV2 with {} adapters: {}", adapters.size(), adapters.keySet());
    }

    @Transactional
    public <P> RelationshipResponse upsertRelationship(RelationshipDataSource adapterName,
            RelationshipRequest<P> request) {
        log.info("[{}] Upserting relationship: {} <-> {}", adapterName, request.getFrom().getIdentifier(),
                request.getTo().getIdentifier());

        RelationshipAdapter adapter = getAdapter(adapterName);

        RelationshipNode fromNode = adapter.resolveFromNode(request.getFrom());
        RelationshipNode toNode = adapter.resolveToNode(request.getTo());
        RelationshipProperties properties = adapter.resolveRelationshipProperties(request.getProperties());
        properties.setRelationshipDataSource(adapterName);

        Criteria criteria = adapter.baseIdentityCriteria(adapterName, request.getFrom(), request.getTo());
        adapter.customUpsertCriteria(request, criteria);

        Relationship existingRelationship = mongoRepository.findOneByCriteria(criteria, Relationship.class);

        Relationship relationship;

        if (existingRelationship != null) {
            log.info("[{}] Found existing relationship, updating with ID={}", adapterName,
                    existingRelationship.getId());

            JsonNode originalRelationship = deepObjectComparator.toTypedTree(existingRelationship);

            relationship = existingRelationship;
            relationship.setFrom(fromNode);
            relationship.setTo(toNode);
            relationship.setProperties(properties);
            relationship.setStartDate(request.getStartDate());
            relationship.setEndDate(request.getEndDate());

            if (!deepObjectComparator.deepEquals(originalRelationship,
                    deepObjectComparator.toTypedTree(relationship))) {
                mongoRepository.save(relationship);

                log.info("[{}] Successfully updated relationship with ID={}", adapterName, relationship.getId());
            } else {
                log.info("[{}] No changes detected for relationship with ID={}", adapterName, relationship.getId());
            }
        } else {
            log.info("[{}] No existing relationship found, creating new one", adapterName);

            relationship = Relationship.builder().from(fromNode).to(toNode).properties(properties)
                    .startDate(request.getStartDate()).endDate(request.getEndDate()).build();

            mongoRepository.insert(relationship);

            log.info("[{}] Successfully inserted relationship with ID={}", adapterName, relationship.getId());
        }

        return relationshipMapper.toRelationshipResponse(relationship, objectMapper);
    }

    @Transactional
    public <P> RelationshipResponse expireRelationship(RelationshipDataSource adapterName,
            RelationshipExpireRequest<P> request) {
        log.info("[{}] Expiring relationship: {} <-> {}", adapterName, request.getFrom().getIdentifier(),
                request.getTo().getIdentifier());

        RelationshipAdapter adapter = getAdapter(adapterName);

        Criteria criteria = adapter.baseIdentityCriteria(adapterName, request.getFrom(), request.getTo());
        adapter.customExpireCriteria(request, criteria);

        Relationship relationship = mongoRepository.findOneByCriteria(criteria, Relationship.class);

        if (relationship == null) {
            throw new NotFoundException(
                    "[%s] No relationship found to expire by matching criteria".formatted(adapterName));
        }

        adapter.enrichRelationshipProperties(relationship.getProperties());
        relationship.setEndDate(LocalDate.now(ZoneOffset.UTC));

        mongoRepository.save(relationship);
        adapter.enrichNodes(List.of(relationship));

        log.info("[{}] Successfully expired relationship with ID={}", adapterName, relationship.getId());

        return relationshipMapper.toRelationshipResponse(relationship, objectMapper);
    }

    @Transactional
    public RelationshipResponse expireRelationshipById(RelationshipDataSource adapterName, String relationshipId) {
        log.info("[{}] Expiring relationship by ID={}", adapterName, relationshipId);

        RelationshipAdapter adapter = getAdapter(adapterName);

        Criteria criteria = adapter.baseByIdCriteria(adapterName, relationshipId);

        Relationship relationship = mongoRepository.findOneByCriteria(criteria, Relationship.class);

        if (relationship == null) {
            throw new NotFoundException(
                    "[%s] No active relationship found with ID=%s".formatted(adapterName, relationshipId));
        }

        adapter.enrichRelationshipProperties(relationship.getProperties());
        relationship.setEndDate(LocalDate.now(ZoneOffset.UTC));

        mongoRepository.save(relationship);
        adapter.enrichNodes(List.of(relationship));

        log.info("[{}] Successfully expired relationship by ID={}", adapterName, relationship.getId());

        return relationshipMapper.toRelationshipResponse(relationship, objectMapper);
    }

    // @Transactional(readOnly = true)
    // public <P> PaginatedResponse<RelationshipResponse>
    // searchRelationships(RelationshipDataSource adapterName,
    // RelationshipSearchRequest<P> request, PaginationRequestDto pagination) {
    // log.info("[{}] Fetching relationships with search criteria", adapterName);
    //
    // RelationshipAdapter adapter = getAdapter(adapterName);
    //
    // Criteria criteria = adapter.defaultSearchCriteria(adapterName, request);
    // adapter.buildSearchCriteria(request, criteria);
    //
    // Pageable pageable = PaginationUtil.createPageable(pagination.getPage(),
    // pagination.getPageSize(),
    // Sort.unsorted());
    //
    // Page<@NonNull Relationship> relationshipPage =
    // mongoRepository.findByCriteria(criteria, pageable,
    // Relationship.class);
    //
    // log.info("[{}] Found {} relationships matching criteria", adapterName,
    // relationshipPage.getTotalElements());
    //
    // List<Relationship> relationships = relationshipPage.getContent();
    // Map<String, NodeSummary> summaries = adapter.enrichNodes(relationships);
    //
    // return PaginatedResponse.fromPage(relationshipPage,
    // relationship -> enrichNodeProperties(
    // relationshipMapper.toRelationshipResponse(relationship, objectMapper),
    // relationship,
    // summaries));
    // }

    private RelationshipAdapter getAdapter(RelationshipDataSource adapterName) {
        return Optional.ofNullable(adapters.get(adapterName)).orElseThrow(() -> new IllegalArgumentException(
                "Unknown adapter: %s. Available adapters: %s".formatted(adapterName, adapters.keySet())));
    }
}
