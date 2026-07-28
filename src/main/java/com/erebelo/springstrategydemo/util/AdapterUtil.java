package com.erebelo.springstrategydemo.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.query.Criteria;

@UtilityClass
public class AdapterUtil {

    /**
     * Adds a criteria condition only if the supplied value is present (not null).
     *
     * @param criteria
     *            the MongoDB criteria to add the condition to
     * @param key
     *            the field name to filter by
     * @param supplier
     *            the supplier that provides the value to check
     * @param <T>
     *            the type of the supplied value
     */
    public static <T> void addIfPresent(Criteria criteria, String key, Supplier<T> supplier) {

        Optional.ofNullable(supplier).map(Supplier::get).ifPresent(value -> criteria.and(key).is(value));
    }

    /**
     * Maps a source object to a target type using the provided mapper function,
     * returning null if the source is null.
     *
     * @param source
     *            the source object to map
     * @param mapper
     *            the function to apply to the source
     * @param <T>
     *            the source type
     * @param <U>
     *            the target type
     * @return the mapped result, or null if source is null
     */
    public static <T, U> U mapIfNoNull(T source, Function<T, U> mapper) {
        return Optional.ofNullable(source).map(mapper).orElse(null);
    }
}
