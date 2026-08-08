package com.erebelo.springstrategydemo.model.enums.relationship;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RelationshipAdapterType {

    NOVA_SELLING_RELATIONSHIP("NOVA", "SELLING_RELATIONSHIP"),

    NOVA_NON_SELLING_RELATIONSHIP("NOVA", "NON_SELLING_RELATIONSHIP");

    private final String dataSource;
    private final String label;

}
