package org.sevosmart.com.sevosmartbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.sevosmart.com.sevosmartbackend.enums.Role;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "user")
// @TypeAlias("SellerData")
public class Admin extends User {
    @DBRef
    @JsonIgnore
    @JsonBackReference
    private List<Product> products = new ArrayList<>();

    @Builder(builderMethodName = "adminBuilder")
    public Admin(String id, String firstname, String lastname, String email, String password, Role role,
                  List<Product> products, List<String> productsId) {
        super(id, firstname, lastname, email, password, role);
        this.products = products;
    }
}
