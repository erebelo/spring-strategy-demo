package com.erebelo.springstrategydemo.model.enums.relationship;

import java.util.Map;
import java.util.Set;

public enum RelationshipNodeType {

    CONTRACT_NODE, ORGANIZATION_NODE;

    private static final Map<RelationshipDataSource, Set<RelationshipNodeType>> FROM_NODE_TYPES_MAP = Map.of(
            RelationshipDataSource.NOVA_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.CONTRACT_NODE),
            RelationshipDataSource.NOVA_NON_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.ORGANIZATION_NODE));

    private static final Map<RelationshipDataSource, Set<RelationshipNodeType>> TO_NODE_TYPES_MAP = Map.of(
            RelationshipDataSource.NOVA_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.CONTRACT_NODE),
            RelationshipDataSource.NOVA_NON_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.CONTRACT_NODE));

    public static Set<RelationshipNodeType> fromNodeTypes(RelationshipDataSource adapterName) {
        return FROM_NODE_TYPES_MAP.getOrDefault(adapterName, Set.of());
    }

    public static Set<RelationshipNodeType> toNodeTypes(RelationshipDataSource adapterName) {
        return TO_NODE_TYPES_MAP.getOrDefault(adapterName, Set.of());
    }
}
