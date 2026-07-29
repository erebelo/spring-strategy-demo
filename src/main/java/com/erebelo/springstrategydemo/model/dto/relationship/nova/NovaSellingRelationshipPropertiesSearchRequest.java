package com.erebelo.springstrategydemo.model.dto.relationship.nova;

import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaRelationshipStatus;
import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaSellingRelationshipType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaSellingRelationshipPropertiesSearchRequest {

    private NovaRelationshipStatus relationshipStatus;
    private NovaSellingRelationshipType relationshipType;

}
