package org.sevosmart.com.sevosmartbackend.model;

import lombok.*;
import org.sevosmart.com.sevosmartbackend.enums.Role;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
// @TypeAlias("CustomerData")
public class Customer extends User {
    @DBRef
    private List<CartItems> cartItems = new ArrayList<>();

    @Builder(builderMethodName = "customerBuilder")
    public Customer(String id, String firstname, String lastname, String email, String password, Role role,
            List<CartItems> cartItems) {
        super(id, firstname, lastname, email, password, role);
        this.cartItems = cartItems;
    }
}