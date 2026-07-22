package com.erebelo.springstrategydemo.model.dto.relationship.nova.selling.request;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.enumeration.NovaRelationshipStatusDtoEnum;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.selling.enumeration.NovaSellingRelationshipTypeDtoEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
public class NovaSellingRelationshipPropertiesRequest {

    @NotNull
    private NovaRelationshipStatusDtoEnum relationshipStatus;

    @NotNull
    private NovaSellingRelationshipTypeDtoEnum relationshipType;

}
