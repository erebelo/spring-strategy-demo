package com.erebelo.springstrategydemo.service;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelationshipService {

    <P> RelationshipResponse upsertRelationship(RelationshipAdapterType adapterType, RelationshipRequest<P> request);

    <P> RelationshipResponse expireRelationship(RelationshipAdapterType adapterType,
            RelationshipExpireRequest<P> request);

    RelationshipResponse expireRelationshipById(RelationshipAdapterType adapterType, String relationshipId);

    <P> Page<@NonNull RelationshipResponse> searchRelationships(RelationshipAdapterType adapterType,
            RelationshipSearchRequest<P> request, Pageable pageable);

}
