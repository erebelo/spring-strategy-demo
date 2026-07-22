package com.erebelo.springstrategydemo.model.dto.relationship.nova.selling.request.search;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.enumeration.NovaRelationshipStatusDtoEnum;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.selling.enumeration.NovaSellingRelationshipTypeDtoEnum;
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

    private NovaRelationshipStatusDtoEnum relationshipStatus;
    private NovaSellingRelationshipTypeDtoEnum relationshipType;

}
