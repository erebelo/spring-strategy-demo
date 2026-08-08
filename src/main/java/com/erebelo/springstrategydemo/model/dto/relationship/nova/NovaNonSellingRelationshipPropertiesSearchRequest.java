package com.erebelo.springstrategydemo.model.dto.relationship.nova;

import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaNonSellingRelationshipType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class NovaNonSellingRelationshipPropertiesSearchRequest extends RelationshipPropertiesSearchRequest {

    private NovaNonSellingRelationshipType relationshipType;

}
