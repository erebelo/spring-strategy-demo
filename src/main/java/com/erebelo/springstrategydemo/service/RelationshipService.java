package com.erebelo.springstrategydemo.service;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelationshipService {

    <P extends RelationshipPropertiesRequest> RelationshipResponse upsertRelationship(RelationshipRequest<P> request);

    <P> RelationshipResponse expireRelationship(RelationshipExpireRequest<P> request);

    RelationshipResponse expireRelationshipById(String relationshipId, RelationshipAdapterType adapterType);

    <P extends RelationshipPropertiesSearchRequest> Page<@NonNull RelationshipResponse> searchRelationships(
            RelationshipSearchRequest<P> request, Pageable pageable);

}
