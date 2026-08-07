package com.erebelo.springstrategydemo.model.entity.relationship;

import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipNodeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelationshipNode {

    @NotNull
    private RelationshipNodeType type;

    @NotBlank
    private String identifier;

    @Transient
    private Map<String, Object> properties;

}
