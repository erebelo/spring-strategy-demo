package com.erebelo.springstrategydemo.model.dto.relationship.response;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import java.util.Set;

public record RelationshipDataSourceResponse(Set<RelationshipDataSource> relationshipDataSources) {
}
