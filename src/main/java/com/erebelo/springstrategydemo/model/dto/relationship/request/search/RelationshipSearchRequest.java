package com.erebelo.springstrategydemo.model.dto.relationship.request.search;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
public class RelationshipSearchRequest<P extends RelationshipPropertiesSearchRequest> {

    @Valid
    private RelationshipNodeSearchRequest from;

    @Valid
    private RelationshipNodeSearchRequest to;

    @Valid
    private P properties;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    private RelationshipAdapterType adapterType;

}
