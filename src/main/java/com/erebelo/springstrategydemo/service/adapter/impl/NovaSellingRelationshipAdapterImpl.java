package com.erebelo.springstrategydemo.service.adapter.impl;

import com.erebelo.springstrategydemo.exception.model.NotFoundException;
import com.erebelo.springstrategydemo.mapper.NovaRelationshipMapper;
import com.erebelo.springstrategydemo.mapper.RelationshipMapper;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaNonSellingRelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
    private final RelationshipMapper relationshipMapper;
    private final NovaRelationshipMapper novaRelationshipMapper;
    private final ObjectMapper objectMapper;

    private static final String[] CONTRACT_FIELDS = {"role", "productType"};

    protected NovaSellingRelationshipAdapterImpl(Validator validator, MongoRepository mongoRepository,
            RelationshipMapper relationshipMapper, NovaRelationshipMapper novaRelationshipMapper,
            ObjectMapper objectMapper) {
        super(validator);
        this.mongoRepository = mongoRepository;
        this.relationshipMapper = relationshipMapper;
        this.novaRelationshipMapper = novaRelationshipMapper;
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

        return relationshipMapper.toRelationshipNode(nodeRequest, contract, objectMapper);
    }

    @Override
    public <P> RelationshipProperties resolveRelationshipProperties(P properties) {
        log.debug("[{}] Resolving relationship properties.", getAdapterType());

        NovaSellingRelationshipPropertiesRequest novaRelPropertiesRequest = (NovaSellingRelationshipPropertiesRequest) properties;

        validatePropertiesRequest(novaRelPropertiesRequest);

        return novaRelationshipMapper.toNovaSellingRelationshipProperties(novaRelPropertiesRequest);
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
                .collect(Collectors.toMap(Contract::getReferenceId, Function.identity()));

        for (Relationship relationship : relationships) {
            relationship.setFrom(relationshipMapper.enrichRelationshipNode(relationship.getFrom(),
                    contractsByReferenceId.get(relationship.getFrom().getIdentifier()), objectMapper));

            relationship.setTo(relationshipMapper.enrichRelationshipNode(relationship.getTo(),
                    contractsByReferenceId.get(relationship.getTo().getIdentifier()), objectMapper));
        }
    }

    @Override
    public <P extends RelationshipPropertiesSearchRequest> void customSearchCriteria(
            RelationshipSearchRequest<P> request, Criteria criteria) {
        log.debug("[{}] Customizing search criteria.", getAdapterType());

        AdapterUtils.addIfPresent(criteria, "from.type",
                () -> AdapterUtils.mapIfNoNull(request.getFrom(), RelationshipNodeSearchRequest::getType));
        AdapterUtils.addIfPresent(criteria, "from.identifier",
                () -> AdapterUtils.mapIfNoNull(request.getFrom(), RelationshipNodeSearchRequest::getIdentifier));
        AdapterUtils.addIfPresent(criteria, "to.type",
                () -> AdapterUtils.mapIfNoNull(request.getTo(), RelationshipNodeSearchRequest::getType));
        AdapterUtils.addIfPresent(criteria, "to.identifier",
                () -> AdapterUtils.mapIfNoNull(request.getTo(), RelationshipNodeSearchRequest::getIdentifier));

        NovaNonSellingRelationshipPropertiesSearchRequest novaRelPropertiesSearchRequest = (NovaNonSellingRelationshipPropertiesSearchRequest) request
                .getProperties();

        if (novaRelPropertiesSearchRequest != null) {
            AdapterUtils.addIfPresent(criteria, "properties.relationshipType",
                    novaRelPropertiesSearchRequest::getRelationshipType);
        }
    }
}
