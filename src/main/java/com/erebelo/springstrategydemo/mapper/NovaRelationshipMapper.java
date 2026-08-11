package com.erebelo.springstrategydemo.mapper;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.entity.contract.Contract;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.NovaSellingRelationshipProperties;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tools.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, uses = RelationshipMapper.class)
public interface NovaRelationshipMapper {

    /*
     * RelationshipNode mappings.
     */

    @Mapping(target = "properties", source = "contract", qualifiedByName = "objectToProperties")
    RelationshipNode toRelationshipNode(RelationshipNodeRequest nodeRequest, Contract contract,
            @Context ObjectMapper objectMapper);

    @Mapping(target = "properties", source = "contract", qualifiedByName = "objectToProperties")
    RelationshipNode enrichRelationshipNode(RelationshipNode node, Contract contract,
            @Context ObjectMapper objectMapper);

    /*
     * NovaSellingRelationshipProperties mappings.
     */

    NovaSellingRelationshipProperties toNovaSellingRelationshipProperties(
            NovaSellingRelationshipPropertiesRequest novaSellingRelationshipPropertiesRequest);

}
