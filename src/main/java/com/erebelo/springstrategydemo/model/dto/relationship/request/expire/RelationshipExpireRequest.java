package com.erebelo.springstrategydemo.model.dto.relationship.request.expire;

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

    @Valid
    @NotNull
    private RelationshipNodeExpireRequest from;

    @Valid
    @NotNull
    private RelationshipNodeExpireRequest to;

    @Valid
    private P properties;

}
