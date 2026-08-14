package com.erebelo.springstrategydemo.service.adapter;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.entity.relationship.Relationship;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipNode;
import com.erebelo.springstrategydemo.model.entity.relationship.RelationshipProperties;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;

public interface RelationshipAdapter {

    /*
     * Core adapter operations.
     */

    RelationshipAdapterType getAdapterType();

    RelationshipNode resolveFromNode(RelationshipNodeRequest fromNode);

    RelationshipNode resolveToNode(RelationshipNodeRequest toNode);

    <P> RelationshipProperties resolveRelationshipProperties(P properties);

    <P> void enrichRelationshipProperties(P properties);

    void enrichRelationshipNodeProperties(List<Relationship> relationships);

    /*
     * Base criteria creation.
     */

    Criteria baseIdentityCriteria(RelationshipAdapterType adapterType, RelationshipNodeRequest from,
            RelationshipNodeRequest to);

    Criteria baseByIdCriteria(RelationshipAdapterType adapterType, String id);

    <P> Criteria baseSearchCriteria(RelationshipAdapterType adapterType, RelationshipSearchRequest<P> request);

    /*
     * Optional criteria customization.
     */

    default <P> Criteria customUpsertCriteria(RelationshipRequest<P> request, Criteria criteria) {
        return criteria;
    }

    default <P> Criteria customExpireCriteria(RelationshipExpireRequest<P> request, Criteria criteria) {
        return criteria;
    }

    default <P> Criteria customSearchCriteria(RelationshipSearchRequest<P> request, Criteria criteria) {
        return criteria;
    }
}
