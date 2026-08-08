package com.erebelo.springstrategydemo.model.dto.relationship.nova;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaSellingRelationshipType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaSellingRelationshipPropertiesRequest extends RelationshipPropertiesRequest {

    @NotNull
    private NovaSellingRelationshipType relationshipType;

}
