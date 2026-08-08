package com.erebelo.springstrategydemo.model.entity.relationship;

import com.erebelo.springstrategydemo.model.entity.BaseEntity;
import com.erebelo.springstrategydemo.model.enums.relationship.RelationshipAdapterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "relationships")
public class Relationship extends BaseEntity {

    @Id
    private String id;

    @Valid
    @NotNull
    private RelationshipNode from;

    @Valid
    @NotNull
    private RelationshipNode to;

    @Valid
    @NotNull
    private RelationshipProperties properties;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    private RelationshipAdapterType adapterType;

}
