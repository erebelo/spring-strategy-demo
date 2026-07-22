package com.erebelo.springstrategydemo.model.entity.relationship.nova.nonselling;

import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.enumeration.NovaRelationshipLabelEnum;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.enumeration.NovaRelationshipStatusEnum;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.nonselling.enumeration.NovaNonSellingRelationshipTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaNonSellingRelationshipProperties extends RelationshipProperties {

    @NotNull
    private NovaRelationshipStatusEnum relationshipStatus;

    @NotNull
    private NovaNonSellingRelationshipTypeEnum relationshipType;

    @NotNull
    private NovaRelationshipLabelEnum relationshipLabel;

}
