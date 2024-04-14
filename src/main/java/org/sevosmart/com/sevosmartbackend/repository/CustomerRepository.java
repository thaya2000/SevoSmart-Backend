package org.sevosmart.com.sevosmartbackend.repository;


import org.sevosmart.com.sevosmartbackend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}
