package com.erebelo.springstrategydemo.mapper;

import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipNodeResponse;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RelationshipMapper {

    /*
     * RelationshipResponse and RelationshipNodeResponse mappings.
     */

    @Mapping(target = "properties", source = "properties", qualifiedByName = "objectToProperties")
    RelationshipResponse toRelationshipResponse(Relationship relationship, @Context ObjectMapper objectMapper);

    RelationshipNodeResponse toRelationshipNodeResponse(RelationshipNode node);

    /*
     * Property mappings.
     */

    @Named("objectToProperties")
    default Map<String, Object> objectToProperties(Object source, @Context ObjectMapper objectMapper) {
        if (source == null) {
            return null;
        }

        Map<String, Object> properties = objectMapper.convertValue(source, new TypeReference<>() {
        });

        return properties.entrySet().stream().filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
