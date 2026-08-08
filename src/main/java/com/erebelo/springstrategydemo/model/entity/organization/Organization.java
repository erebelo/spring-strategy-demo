package com.erebelo.springstrategydemo.model.entity.organization;

import com.erebelo.springstrategydemo.model.entity.BaseEntity;
import com.erebelo.springstrategydemo.model.enums.organization.OrganizationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "organizations")
public class Organization extends BaseEntity {

    @Id
    private String id;

    @NotBlank
    private String orgRefId;

    @NotBlank
    private String name;

    @NotNull
    private OrganizationStatus status;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

}
