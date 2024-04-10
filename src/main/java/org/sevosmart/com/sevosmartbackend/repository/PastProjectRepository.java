package org.sevosmart.com.sevosmartbackend.repository;

import org.sevosmart.com.sevosmartbackend.model.PastProjects;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PastProjectRepository extends MongoRepository<PastProjects, String> {
}
