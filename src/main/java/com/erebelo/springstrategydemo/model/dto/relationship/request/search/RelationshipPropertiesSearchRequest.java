package com.erebelo.springstrategydemo.model.dto.relationship.request.search;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*
 * Polymorphic base class for RelationshipSearchRequest properties.
 */
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RelationshipPropertiesSearchRequest {

    private RelationshipStatus relationshipStatus;

}
