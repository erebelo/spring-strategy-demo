package com.erebelo.springstrategydemo.service.adapter;

import com.erebelo.springstrategydemo.exception.model.BadRequestException;
import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipNodeRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipStatus;
import com.erebelo.springstrategydemo.util.AdapterUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.core.query.Criteria;

public abstract class AbstractRelationshipAdapter implements RelationshipAdapter {

    private final Validator validator;

    protected AbstractRelationshipAdapter(Validator validator) {
        this.validator = validator;
    }

    @Override
    public final Criteria baseIdentityCriteria(RelationshipAdapterType adapterType, RelationshipNodeRequest from,
            RelationshipNodeRequest to) {
        return Criteria.where("from.type").is(from.getType()).and("from.identifier").is(from.getIdentifier())
                .and("to.type").is(to.getType()).and("to.identifier").is(to.getIdentifier())
                .and("properties.relationshipStatus").ne(RelationshipStatus.EXPIRED).and("adapterType.dataSource")
                .is(adapterType.getDataSource()).and("adapterType.label").is(adapterType.getLabel());
    }

    @Override
    public final Criteria baseByIdCriteria(RelationshipAdapterType adapterType, String id) {
        return Criteria.where("_id").is(id).and("properties.relationshipStatus").ne(RelationshipStatus.EXPIRED)
                .and("adapterType.dataSource").is(adapterType.getDataSource()).and("adapterType.label")
                .is(adapterType.getLabel());
    }

    @Override
    public final <P> Criteria baseSearchCriteria(RelationshipAdapterType adapterType,
            RelationshipSearchRequest<P> request) {
        List<Criteria> expressions = new ArrayList<>();

        expressions.add(Criteria.where("adapterType.dataSource").is(adapterType.getDataSource()));
        expressions.add(Criteria.where("adapterType.label").is(adapterType.getLabel()));

        AdapterUtils.addIfPresent(expressions, "startDate", request::getStartDate);
        AdapterUtils.addIfPresent(expressions, "endDate", request::getEndDate);

        return new Criteria().andOperator(expressions.toArray(new Criteria[0]));
    }

    protected final void validateNodeType(RelationshipNodeType nodeType, boolean isFromNode,
            RelationshipAdapterType adapterType) {
        Set<RelationshipNodeType> validTypes = isFromNode
                ? RelationshipNodeType.fromNodeTypes(adapterType)
                : RelationshipNodeType.toNodeTypes(adapterType);

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
