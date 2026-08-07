package com.erebelo.springstrategydemo.service.adapter;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;

public interface RelationshipAdapter {

    RelationshipDataSource getAdapterName();

    RelationshipNode resolveFromNode(RelationshipNodeRequest fromNode);

    RelationshipNode resolveToNode(RelationshipNodeRequest toNode);

    <P> RelationshipProperties resolveRelationshipProperties(P properties);

    void enrichNodes(List<Relationship> relationships);

    <P> void resolveExpireRelationshipProperties(P properties);

    /**
     * Optional adapter-specific criteria customization.
     */
    default <P> void buildUpsertCriteria(RelationshipRequest<P> request, Criteria criteria) {
    }

    /**
     * Optional adapter-specific criteria customization.
     */
    default <P> void buildSearchCriteria(RelationshipSearchRequest<P> request, Criteria criteria) {
    }

    /**
     * Optional adapter-specific criteria customization.
     */
    default <P> void buildExpireCriteria(RelationshipExpireRequest<P> request, Criteria criteria) {
    }
}
