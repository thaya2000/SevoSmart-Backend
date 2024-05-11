package org.sevosmart.com.sevosmartbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {
    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String district;
    private String phoneNo;
}
