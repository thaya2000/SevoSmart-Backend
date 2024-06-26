package org.sevosmart.com.sevosmartbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart_items")
public class CartItems {
    @Id
    private String id;

    @DBRef
    @EqualsAndHashCode.Exclude
    private Product product;

    @JsonIgnore
    @DBRef
    @EqualsAndHashCode.Exclude
    private Customer customer;

//    private String customerId;

    private int quantity;
}
