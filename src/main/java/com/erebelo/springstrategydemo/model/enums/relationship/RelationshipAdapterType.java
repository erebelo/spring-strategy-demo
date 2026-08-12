package com.erebelo.springstrategydemo.model.enums.relationship;

import com.erebelo.springstrategydemo.config.mongo.converter.DocumentEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RelationshipAdapterType implements DocumentEnum {

    NOVA_SELLING_RELATIONSHIP("NOVA", "SELLING_RELATIONSHIP"), NOVA_NON_SELLING_RELATIONSHIP("NOVA",
            "NON_SELLING_RELATIONSHIP");

    private final String dataSource;
    private final String label;

    public static final String NOVA_SELLING_RELATIONSHIP_NAME = "NOVA_SELLING_RELATIONSHIP";
    public static final String NOVA_NON_SELLING_RELATIONSHIP_NAME = "NOVA_NON_SELLING_RELATIONSHIP";

}
