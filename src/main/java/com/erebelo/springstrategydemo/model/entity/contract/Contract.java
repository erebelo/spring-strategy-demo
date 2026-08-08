package com.erebelo.springstrategydemo.model.entity.contract;

import com.erebelo.springstrategydemo.model.entity.BaseEntity;
import com.erebelo.springstrategydemo.model.enums.contract.ContractBusinessChannel;
import com.erebelo.springstrategydemo.model.enums.contract.ContractProductType;
import com.erebelo.springstrategydemo.model.enums.contract.ContractRole;
import com.erebelo.springstrategydemo.model.enums.contract.ContractStatus;
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
@Document(collection = "contracts")
public class Contract extends BaseEntity {

    @Id
    private String id;

    @NotBlank
    private String referenceId;

    @NotBlank
    private String profileId;

    @NotNull
    private ContractRole role;

    @NotNull
    private ContractBusinessChannel businessChannel;

    @NotNull
    private ContractProductType productType;

    @NotNull
    private ContractStatus status;

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

}
