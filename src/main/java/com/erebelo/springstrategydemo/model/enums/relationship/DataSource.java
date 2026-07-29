package com.erebelo.springstrategydemo.model.enums.relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DataSource {

    @JsonProperty("NOVA")
    NOVA_SELLING_RELATIONSHIP("NOVA Selling Relationship", "NOVA"),

    @JsonProperty("NOVA")
    NOVA_NON_SELLING_RELATIONSHIP("NOVA Non-Selling Relationship", "NOVA");

    private final String code;
    private final String value;

    private static final Map<String, DataSource> ENUM_MAP = initMap();

    private static Map<String, DataSource> initMap() {
        Map<String, DataSource> map = new HashMap<>();
        for (DataSource instance : DataSource.values()) {
            map.put(instance.getCode(), instance);
        }
        return Collections.unmodifiableMap(map);
    }

    public static DataSource fromCode(String code) {
        return ENUM_MAP.get(code);
    }

    @Override
    public String toString() {
        return code;
    }
}
