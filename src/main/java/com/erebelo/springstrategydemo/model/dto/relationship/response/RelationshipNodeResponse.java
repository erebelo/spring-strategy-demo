package com.erebelo.springstrategydemo.model.dto.relationship.response;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
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
public class RelationshipNodeResponse {

    private RelationshipNodeType type;
    private String identifier;
    private Map<String, Object> properties;

}
