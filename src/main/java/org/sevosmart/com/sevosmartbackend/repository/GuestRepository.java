package org.sevosmart.com.sevosmartbackend.repository;

import org.sevosmart.com.sevosmartbackend.model.Guest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GuestRepository extends MongoRepository<Guest, String> {

}
