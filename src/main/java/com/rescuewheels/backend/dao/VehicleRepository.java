package com.rescuewheels.backend.dao;

import com.rescuewheels.backend.entity.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    @Query("{ 'ownerId' : ?0 }")
    List<Vehicle> findByOwnerId(String ownerId);

}
