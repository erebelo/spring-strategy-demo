package com.erebelo.springstrategydemo.model.entity.relationship.enumeratoin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DataSourceEnum {

    @JsonProperty("NOVA")
    NOVA_SELLING_RELATIONSHIP("NOVA Selling Relationship", "NOVA"),

    @JsonProperty("NOVA")
    NOVA_NON_SELLING_RELATIONSHIP("NOVA Non-Selling Relationship", "NOVA");

    private final String code;
    private final String value;

    private static final Map<String, DataSourceEnum> ENUM_MAP = initMap();

    private static Map<String, DataSourceEnum> initMap() {
        Map<String, DataSourceEnum> map = new HashMap<>();
        for (DataSourceEnum instance : DataSourceEnum.values()) {
            map.put(instance.getCode(), instance);
        }
        return Collections.unmodifiableMap(map);
    }

    public static DataSourceEnum fromCode(String code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
