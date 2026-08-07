package com.erebelo.springstrategydemo.mapper;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.NovaSellingRelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.nova.NovaRelationshipLabel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, imports = {NovaRelationshipLabel.class})
public interface NovaRelationshipMapper {

    /*
     * NovaSellingRelationshipProperties mappings.
     */

    @Mapping(target = "relationshipLabel", expression = "java(NovaRelationshipLabel.SELLING_RELATIONSHIP)")
    NovaSellingRelationshipProperties toNovaSellingRelationshipProperties(
            NovaSellingRelationshipPropertiesRequest novaSellingRelationshipPropertiesRequest);

}
