package com.rescuewheels.backend.dao;

import com.rescuewheels.backend.entity.EmergencyRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EmergencyRequestRepository extends MongoRepository<EmergencyRequest, String> {

    @Query("{'$or': [{'requestedBy': ?0}, {'responderId': ?0}]}")
    List<EmergencyRequest> findByUserId(String userId);
}
