package com.erebelo.springstrategydemo.model.dto.relationship.request.search;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaNonSellingRelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
 *            Type of relationship-specific properties.
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
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "adapterType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = NovaSellingRelationshipPropertiesSearchRequest.class, name = RelationshipAdapterType.NOVA_SELLING_RELATIONSHIP_NAME),
            @JsonSubTypes.Type(value = NovaNonSellingRelationshipPropertiesSearchRequest.class, name = RelationshipAdapterType.NOVA_NON_SELLING_RELATIONSHIP_NAME)})
    private P properties;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    private RelationshipAdapterType adapterType;

}
