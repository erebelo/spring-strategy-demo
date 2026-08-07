package com.erebelo.springstrategydemo.model.dto.relationship.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
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
public class RelationshipResponse {

    private String id;
    private RelationshipNodeResponse from;
    private RelationshipNodeResponse to;
    private Map<String, Object> properties;
    private LocalDate startDate;
    private LocalDate endDate;

}
