package com.erebelo.springstrategydemo.mapper;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.entity.contract.Contract;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.NovaSellingRelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaRelationshipLabel;
import java.util.Map;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, imports = {NovaRelationshipLabel.class})
public interface NovaRelationshipMapper {

    /*
     * RelationshipNode mapper start here
     */

    @Mapping(target = "properties", source = "contract", qualifiedByName = "mapNodeProperties")
    RelationshipNode toRelationshipNode(RelationshipNodeRequest nodeRequest, Contract contract,
            @Context ObjectMapper objectMapper);

    @Mapping(target = "properties", source = "contract", qualifiedByName = "mapNodeProperties")
    RelationshipNode toRelationshipNode(RelationshipNode node, Contract contract, @Context ObjectMapper objectMapper);

    /*
     * NOVA Selling Relationship properties mapper starts here
     */

    @Mapping(target = "relationshipLabel", expression = "java(NovaRelationshipLabel.SELLING_RELATIONSHIP)")
    NovaSellingRelationshipProperties toNovaSellingRelationshipProperties(
            NovaSellingRelationshipPropertiesRequest novaSellingRelationshipPropertiesRequest);

    /*
     * Properties mapper starts here
     */

    @Named("mapNodeProperties")
    default Map<String, Object> mapNodeProperties(Object properties, @Context ObjectMapper objectMapper) {
        if (properties == null) {
            return null;
        }

        return objectMapper.convertValue(properties, new TypeReference<>() {
        });
    }
}
