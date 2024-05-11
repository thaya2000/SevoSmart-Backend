package org.sevosmart.com.sevosmartbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sevosmart.com.sevosmartbackend.enums.OrderStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String orderId;
    @JsonIgnore
    @DBRef
    private Customer customer;
    private LocalDate orderDate;
    private String addressLineOne;
    private String addressLineTwo;
    private String city;
    private String district;
    private String phoneNo;
    private OrderStatus orderStatus;
    @JsonIgnore
    @DBRef
    private List<CartItems> orderItems;
    private double shippingCost;
    private double totalPrice;
}
