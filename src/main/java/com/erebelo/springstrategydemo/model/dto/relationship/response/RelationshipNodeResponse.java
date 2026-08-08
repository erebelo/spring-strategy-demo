package com.erebelo.springstrategydemo.model.dto.relationship.response;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record RelationshipNodeResponse(RelationshipNodeType type, String identifier, Map<String, Object> properties) {
}
