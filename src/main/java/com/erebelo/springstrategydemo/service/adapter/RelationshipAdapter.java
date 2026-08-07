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

    /*
     * Core adapter operations.
     */

    RelationshipDataSource getAdapterName();

    RelationshipNode resolveFromNode(RelationshipNodeRequest fromNode);

    RelationshipNode resolveToNode(RelationshipNodeRequest toNode);

    <P> RelationshipProperties resolveRelationshipProperties(P properties);

    void enrichNodes(List<Relationship> relationships);

    <P> void resolveExpireRelationshipProperties(P properties);

    /*
     * Base criteria creation.
     */

    Criteria baseIdentityCriteria(RelationshipDataSource adapterName, RelationshipNodeRequest from,
            RelationshipNodeRequest to);

    <P> Criteria baseSearchCriteria(RelationshipDataSource adapterName, RelationshipSearchRequest<P> request);

    Criteria baseByIdCriteria(RelationshipDataSource adapterName, String id);

    /*
     * Optional criteria customization
     */

    default <P> void customUpsertCriteria(RelationshipRequest<P> request, Criteria criteria) {
    }

    default <P> void customSearchCriteria(RelationshipSearchRequest<P> request, Criteria criteria) {
    }

    default <P> void customExpireCriteria(RelationshipExpireRequest<P> request, Criteria criteria) {
    }
}
