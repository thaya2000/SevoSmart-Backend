package org.sevosmart.com.sevosmartbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillBenefitsResponse {
    private Double earnMoney;
    private Double saveMoney;
}
