package com.erebelo.springstrategydemo.service.adapter.impl;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import com.erebelo.springstrategydemo.service.adapter.AbstractRelationshipAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NovaNonSellingRelationshipAdapterImpl extends AbstractRelationshipAdapter {

    @Override
    public RelationshipDataSource getAdapterName() {
        return null;
    }

    @Override
    public ResolvedNode resolveFromNode(RelationshipNodeRequest fromNode) {
        return null;
    }

    @Override
    public ResolvedNode resolveToNode(RelationshipNodeRequest toNode) {
        return null;
    }

    @Override
    public Map<String, NodeSummary> enrichNodes(List<Relationship> relationships) {
        return null;
    }

    @Override
    public <P> RelationshipProperties resolveRelationshipProperties(P properties) {
        return null;
    }

    @Override
    public <P> void resolveExpireRelationshipProperties(P properties) {

    }

    @Override
    public <P> void buildUpsertCriteria(RelationshipRequest<P> request, Criteria criteria) {

    }

    @Override
    public <P> void buildSearchCriteria(RelationshipSearchRequest<P> request, Criteria criteria) {

    }

    @Override
    public <P> void buildExpireCriteria(RelationshipExpireRequest<P> request, Criteria criteria) {

    }
}
