package org.sevosmart.com.sevosmartbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillCalculatorResponse {
    private Double recommendedSolarPower;
    private Integer unitPerMonth;
}
