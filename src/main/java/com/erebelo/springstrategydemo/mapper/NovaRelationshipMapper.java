package com.erebelo.springstrategydemo.mapper;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.entity.relationship.nova.NovaSellingRelationshipProperties;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface NovaRelationshipMapper {

    /*
     * NovaSellingRelationshipProperties mappings.
     */

    NovaSellingRelationshipProperties toNovaSellingRelationshipProperties(
            NovaSellingRelationshipPropertiesRequest novaSellingRelationshipPropertiesRequest);

}
