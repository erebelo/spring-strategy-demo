package com.erebelo.springstrategydemo.service.adapter.impl;

import com.erebelo.springstrategydemo.exception.model.NotFoundException;
import com.erebelo.springstrategydemo.mapper.NovaRelationshipMapper;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.entity.contract.Contract;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
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
    private final NovaRelationshipMapper mapper;
    private final ObjectMapper objectMapper;

    private static final String[] CONTRACT_FIELDS = {"role", "productType"};

    protected NovaSellingRelationshipAdapterImpl(Validator validator, MongoRepository mongoRepository,
            NovaRelationshipMapper mapper, ObjectMapper objectMapper) {
        super(validator);
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
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

    // @Override
    // public RelationshipNode resolveFromNode(RelationshipNodeRequest fromNode) {
    // log.debug("[{}] Resolving from node for type={}, identifier={}",
    // getAdapterName(), fromNode.getType(),
    // fromNode.getIdentifier());
    //
    // validateNodeType(fromNode.getType(), true, getAdapterName());
    //
    // Contract contract = mongoRepository.findOneByField("referenceId",
    // fromNode.getIdentifier(), Contract.class,
    // CONTRACT_FIELDS);
    //
    // if (contract == null) {
    // throw new NotFoundException("from node not found for type=%s,
    // identifier=%s".formatted(fromNode.getType(),
    // fromNode.getIdentifier()));
    // }
    //
    // return mapper.toRelationshipNode(fromNode, contract, objectMapper);
    // }

    // @Override
    // public RelationshipNode resolveToNode(RelationshipNodeRequest toNode) {
    // log.debug("[{}] Resolving to node for type={}, identifier={}",
    // getAdapterName(), toNode.getType(),
    // toNode.getIdentifier());
    //
    // validateNodeType(toNode.getType(), true, getAdapterName());
    //
    // Contract contract = mongoRepository.findOneByField("referenceId",
    // toNode.getIdentifier(), Contract.class,
    // CONTRACT_FIELDS);
    //
    // if (contract == null) {
    // throw new NotFoundException(
    // "to node not found for type=%s, identifier=%s".formatted(toNode.getType(),
    // toNode.getIdentifier()));
    // }
    //
    // return mapper.toRelationshipNode(toNode, contract, objectMapper);
    // }

    private RelationshipNode resolveNode(RelationshipNodeRequest nodeRequest) {
        Contract contract = mongoRepository.findOneByField("referenceId", nodeRequest.getIdentifier(), Contract.class,
                CONTRACT_FIELDS);

        if (contract == null) {
            throw new NotFoundException("Node not found for type=%s, identifier=%s".formatted(nodeRequest.getType(),
                    nodeRequest.getIdentifier()));
        }

        return mapper.toRelationshipNode(nodeRequest, contract, objectMapper);
    }

    @Override
    public <P> RelationshipProperties resolveRelationshipProperties(P properties) {
        log.debug("[{}] Resolving relationship properties", getAdapterName());

        NovaSellingRelationshipPropertiesRequest novaRelProperties = (NovaSellingRelationshipPropertiesRequest) properties;

        validatePropertiesRequest(novaRelProperties);

        return mapper.toNovaSellingRelationshipProperties(novaRelProperties);
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
            relationship.setFrom(mapper.toRelationshipNode(relationship.getFrom(),
                    contractsByReferenceId.get(relationship.getFrom().getIdentifier()), objectMapper));

            relationship.setTo(mapper.toRelationshipNode(relationship.getTo(),
                    contractsByReferenceId.get(relationship.getTo().getIdentifier()), objectMapper));
        }
    }
}
