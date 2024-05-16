package org.sevosmart.com.sevosmartbackend.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderNumber;
    private String customerName;
    private LocalDate orderDate;
    private String customerPhoneNo;
    private String orderStatus;
    private String orderAmount;
    private String orderBillingAddress;
}
