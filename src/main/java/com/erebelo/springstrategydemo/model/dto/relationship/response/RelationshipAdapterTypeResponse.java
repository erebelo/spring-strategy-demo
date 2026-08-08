package com.erebelo.springstrategydemo.model.dto.relationship.response;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import java.util.Set;

public record RelationshipAdapterTypeResponse(Set<RelationshipAdapterType> relationshipAdapterTypes) {
}
