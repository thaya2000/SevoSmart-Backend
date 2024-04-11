package org.sevosmart.com.sevosmartbackend.repository;


import org.sevosmart.com.sevosmartbackend.model.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuyerRepository extends MongoRepository<Buyer, String> {
}
