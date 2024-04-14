package org.sevosmart.com.sevosmartbackend.repository;

import org.sevosmart.com.sevosmartbackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
//    List<Product> findBySellerId(String sellerId);

    List<Product> findByAdminId(String adminId);
}
