package com.erebelo.springstrategydemo.service.adapter.impl;

import com.erebelo.springstrategydemo.exception.model.NotFoundException;
import com.erebelo.springstrategydemo.mapper.NovaRelationshipMapper;
import com.erebelo.springstrategydemo.mapper.RelationshipMapper;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.entity.contract.Contract;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.NovaSellingRelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaRelationshipStatus;
import com.erebelo.springstrategydemo.repository.MongoRepository;
import com.erebelo.springstrategydemo.service.adapter.AbstractRelationshipAdapter;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
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
    public RelationshipDataSource getAdapterName() {
        return RelationshipDataSource.NOVA_SELLING_RELATIONSHIP;
    }

    @Override
    public RelationshipNode resolveFromNode(RelationshipNodeRequest fromNode) {
        validateNodeType(fromNode.getType(), true, getAdapterName());
        return resolveNode(fromNode);
    }

    @Override
    public RelationshipNode resolveToNode(RelationshipNodeRequest toNode) {
        validateNodeType(toNode.getType(), false, getAdapterName());
        return resolveNode(toNode);
    }

    private RelationshipNode resolveNode(RelationshipNodeRequest nodeRequest) {
        log.debug("[{}] Resolving node for type={}, identifier={}", getAdapterName(), nodeRequest.getType(),
                nodeRequest.getIdentifier());

        Contract contract = mongoRepository.findOneByField("referenceId", nodeRequest.getIdentifier(), Contract.class,
                CONTRACT_FIELDS);

        if (contract == null) {
            throw new NotFoundException("Node not found for type=%s, identifier=%s".formatted(nodeRequest.getType(),
                    nodeRequest.getIdentifier()));
        }

        return relationshipMapper.toRelationshipNode(nodeRequest, contract, objectMapper);
    }

    @Override
    public <P> RelationshipProperties resolveRelationshipProperties(P properties) {
        log.debug("[{}] Resolving relationship properties", getAdapterName());

        NovaSellingRelationshipPropertiesRequest novaRelPropertiesRequest = (NovaSellingRelationshipPropertiesRequest) properties;

        validatePropertiesRequest(novaRelPropertiesRequest);

        return novaRelationshipMapper.toNovaSellingRelationshipProperties(novaRelPropertiesRequest);
    }

    @Override
    public <P> void enrichRelationshipProperties(P properties) {
        log.debug("[{}] Enriching relationship properties", getAdapterName());

        NovaSellingRelationshipProperties novaRelProperties = (NovaSellingRelationshipProperties) properties;
        novaRelProperties.setRelationshipStatus(NovaRelationshipStatus.EXPIRED);
    }

    @Override
    public void enrichNodes(List<Relationship> relationships) {
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
}
