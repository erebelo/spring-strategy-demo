package com.erebelo.springstrategydemo.controller;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipAdapterTypeResponse;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
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

    @GetMapping("/adapter-types")
    public RelationshipAdapterTypeResponse adapterTypes() {
        log.info("Listing available relationship adapter types.");

        return new RelationshipAdapterTypeResponse(EnumSet.allOf(RelationshipAdapterType.class));
    }

    @PostMapping
    public RelationshipResponse upsertRelationship(@Valid @RequestBody RelationshipRequest<?> request) {
        log.info("[{}] Upserting relationship.", request.getAdapterType());

        return service.upsertRelationship(request);
    }

    @PostMapping("/expire")
    public RelationshipResponse expireRelationship(@Valid @RequestBody RelationshipExpireRequest<?> request) {
        log.info("[{}] Expiring relationship.", request.getAdapterType());

        return service.expireRelationship(request);
    }

    @PostMapping("/{relationshipId}/expire")
    public RelationshipResponse expireRelationshipById(@PathVariable String relationshipId,
            @RequestParam RelationshipAdapterType adapterType) {
        log.info("[{}] Expiring relationship by ID={}.", adapterType, relationshipId);

        return service.expireRelationshipById(relationshipId, adapterType);
    }

    @PostMapping("/search")
    public Page<@NonNull RelationshipResponse> searchRelationships(
            @Valid @RequestBody RelationshipSearchRequest<?> request, @PageableDefault(size = 20) Pageable pageable) {
        log.info("[{}] Searching relationships.", request.getAdapterType());

        return service.searchRelationships(request, pageable);
    }
}
