package com.erebelo.springstrategydemo.controller;

import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesRequest;
import com.erebelo.springstrategydemo.model.dto.relationship.nova.NovaSellingRelationshipPropertiesSearchRequest;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.erebelo.springstrategydemo.service.RelationshipService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relationships/nova/selling")
public class NovaSellingRelationshipController
        extends
            AbstractRelationshipController<NovaSellingRelationshipPropertiesRequest, Void, NovaSellingRelationshipPropertiesSearchRequest> {

    public NovaSellingRelationshipController(RelationshipService service) {
        super(service, RelationshipAdapterType.NOVA_SELLING_RELATIONSHIP);
    }
}
