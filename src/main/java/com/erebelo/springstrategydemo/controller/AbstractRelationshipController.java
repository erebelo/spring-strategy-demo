package com.erebelo.springstrategydemo.controller;

import com.erebelo.springstrategydemo.model.dto.relationship.request.RelationshipRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.expire.RelationshipExpireRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.request.search.RelationshipSearchRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.response.RelationshipResponse;
import com.erebelo.springstrategydemo.model.dto.response.PageResponse;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.erebelo.springstrategydemo.service.RelationshipService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Validated
public abstract class AbstractRelationshipController<UpsertProperties, ExpireProperties, SearchProperties> {

    private final RelationshipService service;
    private final RelationshipAdapterType adapterType;

    protected AbstractRelationshipController(RelationshipService service, RelationshipAdapterType adapterType) {
        this.service = service;
        this.adapterType = adapterType;
    }

    @PostMapping
    public RelationshipResponse upsertRelationship(@Valid @RequestBody RelationshipRequest<UpsertProperties> request) {
        log.info("[{}] Upserting relationship.", adapterType);

        return service.upsertRelationship(adapterType, request);
    }

    @PostMapping("/expire")
    public RelationshipResponse expireRelationship(
            @Valid @RequestBody RelationshipExpireRequest<ExpireProperties> request) {
        log.info("[{}] Expiring relationship.", adapterType);

        return service.expireRelationship(adapterType, request);
    }

    @PostMapping("/{relationshipId}/expire")
    public RelationshipResponse expireRelationshipById(@PathVariable @NotBlank String relationshipId) {
        log.info("[{}] Expiring relationship by ID={}.", adapterType, relationshipId);

        return service.expireRelationshipById(adapterType, relationshipId);
    }

    @PostMapping("/search")
    public PageResponse<@NonNull RelationshipResponse> searchRelationships(
            @Valid @RequestBody RelationshipSearchRequest<SearchProperties> request,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("[{}] Searching relationships.", adapterType);

        return PageResponse.from(service.searchRelationships(adapterType, request, pageable));
    }
}
