package com.erebelo.springstrategydemo.controller;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipDataSourceResponse;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipDataSource;
import com.erebelo.springstrategydemo.service.RelationshipService;
import jakarta.validation.Valid;
import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/relationships")
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService service;

    @GetMapping("/data-sources")
    public RelationshipDataSourceResponse dataSources() {
        log.info("Listing available relationship data sources.");

        return new RelationshipDataSourceResponse(EnumSet.allOf(RelationshipDataSource.class));
    }

    @PostMapping
    public RelationshipResponse upsertRelationship(@Valid @RequestBody RelationshipRequest<?> request) {
        log.info("[{}] Upserting relationship.", request.getRelationshipDataSource());

        return service.upsertRelationship(request);
    }

    @PostMapping("/expire")
    public RelationshipResponse expireRelationship(@Valid @RequestBody RelationshipExpireRequest<?> request) {
        log.info("[{}] Expiring relationship.", request.getRelationshipDataSource());

        return service.expireRelationship(request);
    }

    @PostMapping("/{relationshipId}/expire")
    public RelationshipResponse expireRelationshipById(@PathVariable String relationshipId,
            @RequestParam RelationshipDataSource relationshipDataSource) {
        log.info("[{}] Expiring relationship by ID={}.", relationshipDataSource, relationshipId);

        return service.expireRelationshipById(relationshipId, relationshipDataSource);
    }

    @PostMapping("/search")
    public Page<@NonNull RelationshipResponse> searchRelationships(
            @Valid @RequestBody RelationshipSearchRequest<?> request, @PageableDefault(size = 20) Pageable pageable) {
        log.info("[{}] Searching relationships.", request.getRelationshipDataSource());

        return service.searchRelationships(request, pageable);
    }
}
