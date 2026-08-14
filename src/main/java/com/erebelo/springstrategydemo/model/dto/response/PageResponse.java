package com.erebelo.springstrategydemo.model.dto.response;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<@NonNull T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages());
    }
}
