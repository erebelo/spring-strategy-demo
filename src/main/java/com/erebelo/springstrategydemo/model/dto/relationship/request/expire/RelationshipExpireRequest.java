package com.erebelo.springstrategydemo.model.dto.relationship.request.expire;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param <P>
 *            Type of relationship properties
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipExpireRequest<P> {

    @NotNull
    private RelationshipDataSource relationshipDataSource;

    @Valid
    @NotNull
    private RelationshipNodeRequest from;

    @Valid
    @NotNull
    private RelationshipNodeRequest to;

    @Valid
    private P properties;

}
