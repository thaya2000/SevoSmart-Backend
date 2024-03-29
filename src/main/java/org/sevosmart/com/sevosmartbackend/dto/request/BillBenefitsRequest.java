package org.sevosmart.com.sevosmartbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillBenefitsRequest {
    private Double monthlyBill;
    private Integer selectedSolarPower;
}
