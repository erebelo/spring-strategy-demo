package com.erebelo.springstrategydemo.model.dto.relationship.nova.nonselling.request.search;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.enumeration.NovaRelationshipStatusDtoEnum;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.nonselling.enumeration.NovaNonSellingRelationshipTypeDtoEnum;
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
public class NovaNonSellingRelationshipPropertiesSearchRequest {

    private NovaRelationshipStatusDtoEnum relationshipStatus;
    private NovaNonSellingRelationshipTypeDtoEnum relationshipType;

}
