package com.erebelo.springstrategydemo.service.adapter;

import com.erebelo.springstrategydemo.exception.model.BadRequestException;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
import com.erebelo.springstrategydemo.util.AdapterUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.data.mongodb.core.query.Criteria;

public abstract class AbstractRelationshipAdapter implements RelationshipAdapter {

    private final Validator validator;

    protected AbstractRelationshipAdapter(Validator validator) {
        this.validator = validator;
    }

    public final <P> Criteria defaultUpsertCriteria(RelationshipDataSource adapterName,
            RelationshipRequest<P> request) {
        Criteria criteria = new Criteria();

        criteria.and("from.type").is(request.getFrom().getType());
        criteria.and("from.identifier").is(request.getFrom().getIdentifier());
        criteria.and("to.type").is(request.getTo().getType());
        criteria.and("to.identifier").is(request.getTo().getIdentifier());
        criteria.and("properties.relationshipStatus").ne("EXPIRED");
        criteria.and("properties.relationshipDataSource").is(adapterName);

        return criteria;
    }

    public final <P> Criteria defaultSearchCriteria(RelationshipDataSource adapterName,
            RelationshipSearchRequest<P> request) {
        Criteria criteria = new Criteria();

        AdapterUtils.addIfPresent(criteria, "startDate", request::getStartDate);
        AdapterUtils.addIfPresent(criteria, "endDate", request::getEndDate);

        criteria.and("properties.relationshipDataSource").is(adapterName);

        return criteria;
    }

    public final <P> Criteria defaultExpireCriteria(RelationshipDataSource adapterName,
            RelationshipExpireRequest<P> request) {
        Criteria criteria = new Criteria();

        criteria.and("from.type").is(request.getFrom().getType());
        criteria.and("from.identifier").is(request.getFrom().getIdentifier());
        criteria.and("to.type").is(request.getTo().getType());
        criteria.and("to.identifier").is(request.getTo().getIdentifier());
        criteria.and("properties.relationshipStatus").ne("EXPIRED");
        criteria.and("properties.relationshipDataSource").is(adapterName);

        return criteria;
    }

    public final Criteria defaultExpireByIdCriteria(RelationshipDataSource adapterName, String id) {
        Criteria criteria = new Criteria();

        criteria.and("_id").is(id);
        criteria.and("properties.relationshipStatus").ne("EXPIRED");
        criteria.and("properties.relationshipDataSource").is(adapterName);

        return criteria;
    }

    protected final void validateNodeType(RelationshipNodeType nodeType, boolean isFromNode,
            RelationshipDataSource adapterName) {
        Set<RelationshipNodeType> validTypes = isFromNode
                ? RelationshipNodeType.fromNodeTypes(adapterName)
                : RelationshipNodeType.toNodeTypes(adapterName);

        if (nodeType == null || !validTypes.contains(nodeType)) {
            String nodeTypeName = isFromNode ? "from.type" : "to.type";
            throw new BadRequestException(
                    "Invalid %s=%s. Accepted type(s): %s".formatted(nodeTypeName, nodeType, validTypes));
        }
    }

    protected final <T> void validatePropertiesRequest(T properties) {
        Set<ConstraintViolation<T>> violations = validator.validate(properties);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
