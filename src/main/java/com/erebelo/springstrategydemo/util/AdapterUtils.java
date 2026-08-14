package com.erebelo.springstrategydemo.util;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.springframework.data.mongodb.core.query.Criteria;

@UtilityClass
public class AdapterUtils {

    /**
     * Adds a criteria condition to the list only if the supplied value is present.
     */
    public static <T> void addIfPresent(List<Criteria> expressions, String key, Supplier<T> supplier) {
        if (supplier == null) {
            return;
        }

        T value = supplier.get();

        if (value != null) {
            expressions.add(Criteria.where(key).is(value));
        }
    }

    /**
     * Maps a source object to a target type only if the source object is present.
     */
    public static <T, U> U mapIfNotNull(T source, Function<T, U> mapper) {
        return source == null ? null : mapper.apply(source);
    }
}
