package com.erebelo.springstrategydemo.model.dto.relationship.request.expire;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
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
public class RelationshipNodeExpireRequest {

    @NotNull
    private RelationshipNodeType type;

    @NotBlank
    private String identifier;

}
