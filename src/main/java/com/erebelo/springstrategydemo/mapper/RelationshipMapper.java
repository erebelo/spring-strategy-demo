package com.erebelo.springstrategydemo.mapper;

import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipNodeResponse;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import java.util.Map;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RelationshipMapper {

    @Mapping(target = "properties", source = "properties", qualifiedByName = "mapProperties")
    RelationshipResponse toRelationshipResponse(Relationship relationship, @Context ObjectMapper objectMapper);

    RelationshipNodeResponse toRelationshipNodeResponse(RelationshipNode relationshipNode,
            @Context ObjectMapper objectMapper);

    @Named("mapProperties")
    default Map<String, Object> mapProperties(Object properties, @Context ObjectMapper objectMapper) {

        if (properties == null) {
            return null;
        }

        return objectMapper.convertValue(properties, new TypeReference<>() {
        });
    }
}
