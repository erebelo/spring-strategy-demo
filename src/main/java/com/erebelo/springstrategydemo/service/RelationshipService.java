package com.erebelo.springstrategydemo.service;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RelationshipService {

    <P> RelationshipResponse upsertRelationship(RelationshipDataSource adapterName, RelationshipRequest<P> request);

    <P> RelationshipResponse expireRelationship(RelationshipDataSource adapterName,
            RelationshipExpireRequest<P> request);

    RelationshipResponse expireRelationshipById(RelationshipDataSource adapterName, String relationshipId);

    <P> Page<@NonNull RelationshipResponse> searchRelationships(RelationshipDataSource adapterName,
            RelationshipSearchRequest<P> request, Pageable pageable);

}
