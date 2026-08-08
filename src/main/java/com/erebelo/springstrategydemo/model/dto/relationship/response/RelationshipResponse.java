package com.erebelo.springstrategydemo.model.dto.relationship.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record RelationshipResponse(String id, RelationshipNodeResponse from, RelationshipNodeResponse to,
        Map<String, Object> properties, LocalDate startDate, LocalDate endDate) {
}
