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
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    private String productName;
    private Double price;
    private Integer quantity;
    private String description;
    private Double discount;
    private String brand;
    private String category;
    private byte[] productImage;

    @JsonIgnore
    @DBRef
    private Admin admin;
}
