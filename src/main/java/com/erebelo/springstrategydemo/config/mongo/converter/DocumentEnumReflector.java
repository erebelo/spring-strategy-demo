package com.erebelo.springstrategydemo.config.mongo.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;

/**
 * Utility to extract attributes from {@link DocumentEnum} instances via
 * reflection for MongoDB persistence.
 * <p>
 * Caches fields for high performance and filters out static or synthetic
 * members.
 * <p>
 * Note: Returns a {@link LinkedHashMap}, but database queries should target
 * individual fields (dot notation) to avoid strict BSON field ordering mismatch
 * issues.
 */
@UtilityClass
class DocumentEnumReflector {

    private static final Map<Class<?>, List<Field>> FIELDS_CACHE = new ConcurrentHashMap<>();

    public static Map<String, Object> extractAttributes(DocumentEnum enumValue) {
        Class<?> clazz = enumValue.getClass();

        List<Field> fields = FIELDS_CACHE.computeIfAbsent(clazz, type -> {
            List<Field> validFields = new ArrayList<>();

            for (Field field : type.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !field.isSynthetic()) {
                    field.setAccessible(true);
                    validFields.add(field);
                }
            }

            return validFields;
        });

        Map<String, Object> attributes = new LinkedHashMap<>();

        for (Field field : fields) {
            try {
                attributes.put(field.getName(), field.get(enumValue));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to extract attribute [%s] from enum [%s] via reflection."
                        .formatted(field.getName(), clazz.getSimpleName()), e);
            }
        }

        return attributes;
    }
}
