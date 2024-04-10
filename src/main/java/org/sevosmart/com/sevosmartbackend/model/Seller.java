package org.sevosmart.com.sevosmartbackend.model;

import lombok.*;
import org.sevosmart.com.sevosmartbackend.enums.Role;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "user")
//@TypeAlias("SellerData")
public class Seller extends User{
    @DBRef
    private List<String> productIds = new ArrayList<>();

    @Builder(builderMethodName = "sellerBuilder")
    public Seller(String id, String firstname, String lastname, String email, String password, Role role, List<String> productIds) {
        super(id, firstname, lastname, email, password, role);
        this.productIds = productIds;
    }
}
