package org.sevosmart.com.sevosmartbackend.repository;

import org.sevosmart.com.sevosmartbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
