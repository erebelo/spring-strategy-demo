package com.erebelo.springstrategydemo.model.dto.relationship.nova.nonselling.request;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.enumeration.NovaRelationshipStatusDtoEnum;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.nonselling.enumeration.NovaNonSellingRelationshipTypeDtoEnum;
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
public class NovaNonSellingRelationshipPropertiesRequest {

    @NotNull
    private NovaRelationshipStatusDtoEnum relationshipStatus;

    @NotNull
    private NovaNonSellingRelationshipTypeDtoEnum relationshipType;

}
