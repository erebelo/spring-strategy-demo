package com.erebelo.springstrategydemo.service.adapter;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;

public interface RelationshipAdapter {

    /**
     * Default operations with shared implementations provided by the abstract base
     * adapter.
     */

    <P> Criteria defaultUpsertCriteria(RelationshipDataSource adapterName, RelationshipRequest<P> request);

    <P> Criteria defaultSearchCriteria(RelationshipDataSource adapterName, RelationshipSearchRequest<P> request);

    <P> Criteria defaultExpireCriteria(RelationshipDataSource adapterName, RelationshipExpireRequest<P> request);

    Criteria defaultExpireByIdCriteria(RelationshipDataSource adapterName, String id);

    /**
     * Operations that must be implemented by concrete adapter strategies.
     */

    RelationshipDataSource getAdapterName();

    ResolvedNode resolveFromNode(RelationshipNodeRequest fromNode);

    ResolvedNode resolveToNode(RelationshipNodeRequest toNode);

    Map<String, NodeSummary> enrichNodes(List<Relationship> relationships);

    <P> RelationshipProperties resolveRelationshipProperties(P properties);

    <P> void resolveExpireRelationshipProperties(P properties);

    <P> void buildUpsertCriteria(RelationshipRequest<P> request, Criteria criteria);

    <P> void buildSearchCriteria(RelationshipSearchRequest<P> request, Criteria criteria);

    <P> void buildExpireCriteria(RelationshipExpireRequest<P> request, Criteria criteria);

}
