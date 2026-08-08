package com.erebelo.springstrategydemo.model.enums.relationship;

import java.util.Map;
import java.util.Set;

public enum RelationshipNodeType {

    CONTRACT_NODE, ORGANIZATION_NODE;

    private static final Map<RelationshipAdapterType, Set<RelationshipNodeType>> FROM_NODE_TYPES_MAP = Map.of(
            RelationshipAdapterType.NOVA_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.CONTRACT_NODE),
            RelationshipAdapterType.NOVA_NON_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.ORGANIZATION_NODE));

    private static final Map<RelationshipAdapterType, Set<RelationshipNodeType>> TO_NODE_TYPES_MAP = Map.of(
            RelationshipAdapterType.NOVA_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.CONTRACT_NODE),
            RelationshipAdapterType.NOVA_NON_SELLING_RELATIONSHIP, Set.of(RelationshipNodeType.CONTRACT_NODE));

    public static Set<RelationshipNodeType> fromNodeTypes(RelationshipAdapterType adapterType) {
        return FROM_NODE_TYPES_MAP.getOrDefault(adapterType, Set.of());
    }

    public static Set<RelationshipNodeType> toNodeTypes(RelationshipAdapterType adapterType) {
        return TO_NODE_TYPES_MAP.getOrDefault(adapterType, Set.of());
    }
}
