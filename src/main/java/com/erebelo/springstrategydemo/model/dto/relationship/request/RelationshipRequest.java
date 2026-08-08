package com.erebelo.springstrategydemo.model.dto.relationship.request;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaNonSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
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
 * Request containing polymorphic relationship properties.
 *
 * <p>
 * The {@code @JsonTypeInfo} and {@code @JsonSubTypes} annotations use
 * {@code adapterType} as the discriminator to determine the concrete type of
 * {@code properties} during JSON deserialization.
 *
 * @param <P>
 *            Type of relationship-specific properties.
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipRequest<P extends RelationshipPropertiesRequest> {

    @Valid
    @NotNull
    private RelationshipNodeRequest from;

    @Valid
    @NotNull
    private RelationshipNodeRequest to;

    @Valid
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "adapterType")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = NovaSellingRelationshipPropertiesRequest.class, name = RelationshipAdapterType.NOVA_SELLING_RELATIONSHIP_NAME),
            @JsonSubTypes.Type(value = NovaNonSellingRelationshipPropertiesRequest.class, name = RelationshipAdapterType.NOVA_NON_SELLING_RELATIONSHIP_NAME)})
    private P properties;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    private RelationshipAdapterType adapterType;

}
