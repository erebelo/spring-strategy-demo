package com.erebelo.springstrategydemo.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 * Simple deep comparator using Jackson.
 *
 * <pre>
 * Supports:
 * - Deep comparison
 * - Polymorphism (runtime type included)
 * - Any POJO (no annotations required)
 *
 * Limitations:
 * - Collection order matters (List, Set, Map)
 * - Not suitable for cyclic object graphs
 * - Not intended for security-critical decisions or untrusted input
 *
 * Comparison semantics:
 * Equality is based on Jackson's serialization view.
 * Fields ignored by Jackson (e.g. transient (Java keyword), @JsonIgnore, etc.)
 * are not included in the comparison.
 * Spring Data @Transient fields will still be serialized and compared.
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class DeepObjectComparator {

    private final ObjectMapper mapper;

    /**
     * Compares two objects deeply by converting them to JSON trees.
     */
    public boolean deepEquals(JsonNode a, JsonNode b) {
        return a.equals(b);
    }

    /**
     * Converts object to JsonNode and injects runtime class to preserve
     * polymorphism.
     */
    public JsonNode toTypedTree(Object obj) {
        if (obj == null) {
            return mapper.nullNode();
        }

        JsonNode tree = mapper.valueToTree(obj);

        if (tree.isObject()) {
            ObjectNode objNode = (ObjectNode) tree;
            objNode.put("@class", obj.getClass().getName());
        }

        return tree;
    }
}
