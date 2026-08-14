package com.erebelo.springstrategydemo.service.adapter.impl;

import com.erebelo.springstrategydemo.exception.model.NotFoundException;
import com.erebelo.springstrategydemo.mapper.NovaRelationshipMapper;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipNodeSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.entity.contract.Contract;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.NovaSellingRelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipStatus;
import com.erebelo.springstrategydemo.repository.MongoRepository;
import com.erebelo.springstrategydemo.service.adapter.AbstractRelationshipAdapter;
import com.erebelo.springstrategydemo.util.AdapterUtils;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class NovaSellingRelationshipAdapterImpl extends AbstractRelationshipAdapter {

    private final MongoRepository mongoRepository;
    private final NovaRelationshipMapper mapper;
    private final ObjectMapper objectMapper;

    private static final String[] CONTRACT_FIELDS = {"referenceId", "role", "productType"};

    protected NovaSellingRelationshipAdapterImpl(Validator validator, MongoRepository mongoRepository,
            NovaRelationshipMapper mapper, ObjectMapper objectMapper) {
        super(validator);
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public RelationshipAdapterType getAdapterType() {
        return RelationshipAdapterType.NOVA_SELLING_RELATIONSHIP;
    }

    @Override
    public RelationshipNode resolveFromNode(RelationshipNodeRequest fromNode) {
        validateNodeType(fromNode.getType(), true, getAdapterType());
        return resolveNode(fromNode);
    }

    @Override
    public RelationshipNode resolveToNode(RelationshipNodeRequest toNode) {
        validateNodeType(toNode.getType(), false, getAdapterType());
        return resolveNode(toNode);
    }

    private RelationshipNode resolveNode(RelationshipNodeRequest nodeRequest) {
        log.debug("[{}] Resolving node. type={}, identifier={}", getAdapterType(), nodeRequest.getType(),
                nodeRequest.getIdentifier());

        Contract contract = mongoRepository.findOneByField("referenceId", nodeRequest.getIdentifier(), Contract.class,
                CONTRACT_FIELDS);

        if (contract == null) {
            throw new NotFoundException("Node not found. type=%s, identifier=%s".formatted(nodeRequest.getType(),
                    nodeRequest.getIdentifier()));
        }

        contract.setReferenceId(null); // Wipe field to exclude from object
        return mapper.toRelationshipNode(nodeRequest, contract, objectMapper);
    }

    @Override
    public <P> RelationshipProperties resolveRelationshipProperties(P properties) {
        log.debug("[{}] Resolving relationship properties.", getAdapterType());

        NovaSellingRelationshipPropertiesRequest novaRelPropertiesRequest = (NovaSellingRelationshipPropertiesRequest) properties;

        validatePropertiesRequest(novaRelPropertiesRequest);

        return mapper.toNovaSellingRelationshipProperties(novaRelPropertiesRequest);
    }

    @Override
    public <P> void enrichRelationshipProperties(P properties) {
        log.debug("[{}] Enriching relationship properties.", getAdapterType());

        NovaSellingRelationshipProperties novaRelProperties = (NovaSellingRelationshipProperties) properties;
        novaRelProperties.setRelationshipStatus(RelationshipStatus.EXPIRED);
    }

    @Override
    public void enrichRelationshipNodeProperties(List<Relationship> relationships) {
        log.debug("[{}] Enriching relationship node properties.", getAdapterType());

        Set<String> nodeIdentifiers = relationships.stream().flatMap(
                relationship -> Stream.of(relationship.getFrom().getIdentifier(), relationship.getTo().getIdentifier()))
                .collect(Collectors.toSet());

        Map<String, Contract> contractsByReferenceId = mongoRepository
                .findAllByField("referenceId", nodeIdentifiers, Contract.class, CONTRACT_FIELDS).stream()
                .collect(Collectors.toMap(Contract::getReferenceId, // Use as map key before wiping
                        contract -> {
                            contract.setReferenceId(null); // Wipe field to exclude from object
                            return contract;
                        }));

        for (Relationship relationship : relationships) {
            relationship.setFrom(mapper.enrichRelationshipNode(relationship.getFrom(),
                    contractsByReferenceId.get(relationship.getFrom().getIdentifier()), objectMapper));

            relationship.setTo(mapper.enrichRelationshipNode(relationship.getTo(),
                    contractsByReferenceId.get(relationship.getTo().getIdentifier()), objectMapper));
        }
    }

    @Override
    public <P extends RelationshipPropertiesSearchRequest> Criteria customSearchCriteria(
            RelationshipSearchRequest<P> request, Criteria criteria) {
        log.debug("[{}] Customizing search criteria.", getAdapterType());

        List<Criteria> expressions = new ArrayList<>();

        AdapterUtils.addIfPresent(expressions, "from.type",
                () -> AdapterUtils.mapIfNotNull(request.getFrom(), RelationshipNodeSearchRequest::getType));
        AdapterUtils.addIfPresent(expressions, "from.identifier",
                () -> AdapterUtils.mapIfNotNull(request.getFrom(), RelationshipNodeSearchRequest::getIdentifier));

        AdapterUtils.addIfPresent(expressions, "to.type",
                () -> AdapterUtils.mapIfNotNull(request.getTo(), RelationshipNodeSearchRequest::getType));
        AdapterUtils.addIfPresent(expressions, "to.identifier",
                () -> AdapterUtils.mapIfNotNull(request.getTo(), RelationshipNodeSearchRequest::getIdentifier));

        AdapterUtils.addIfPresent(expressions, "properties.relationshipType",
                () -> AdapterUtils.mapIfNotNull(
                        (NovaSellingRelationshipPropertiesSearchRequest) request.getProperties(),
                        NovaSellingRelationshipPropertiesSearchRequest::getRelationshipType));

        if (expressions.isEmpty()) {
            return criteria;
        }

        return new Criteria().andOperator(criteria, new Criteria().andOperator(expressions.toArray(new Criteria[0])));
    }
}
