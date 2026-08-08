package com.erebelo.springstrategydemo.model.entity.relationship;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Base class for polymorphic, relationship-specific entity properties.
 */
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RelationshipProperties {

    @NotNull
    private RelationshipStatus relationshipStatus;

}
