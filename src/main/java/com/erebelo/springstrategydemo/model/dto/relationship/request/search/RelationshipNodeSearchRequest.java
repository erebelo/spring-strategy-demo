package com.erebelo.springstrategydemo.model.dto.relationship.request.search;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
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
public class RelationshipNodeSearchRequest {

    private RelationshipNodeType type;
    private String identifier;

}
