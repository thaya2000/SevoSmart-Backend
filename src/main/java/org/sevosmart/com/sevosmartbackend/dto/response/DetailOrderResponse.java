package org.sevosmart.com.sevosmartbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailOrderResponse {
    private String orderNumber;
    private String orderCustomerName;
    private LocalDate orderDate;
    private String orderStatus;
    private String orderAmount;
    private String orderBillingAddress;
    private List<ProductDetail> products;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductDetail {
        private String productName;
        private int productQuantity;
        private String productImageUrl;
    }
}
