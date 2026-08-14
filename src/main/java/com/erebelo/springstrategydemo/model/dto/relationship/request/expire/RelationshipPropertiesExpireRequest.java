package com.erebelo.springstrategydemo.model.dto.relationship.request.expire;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base class for polymorphic, relationship-specific expire properties.
 */
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RelationshipPropertiesExpireRequest {

    private RelationshipStatus relationshipStatus;

}
