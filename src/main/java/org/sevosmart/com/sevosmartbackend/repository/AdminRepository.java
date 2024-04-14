package org.sevosmart.com.sevosmartbackend.repository;

import org.sevosmart.com.sevosmartbackend.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
}
