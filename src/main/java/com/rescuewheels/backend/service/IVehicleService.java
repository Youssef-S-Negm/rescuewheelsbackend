package com.rescuewheels.backend.service;

import com.rescuewheels.backend.entity.Vehicle;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface IVehicleService {

    Vehicle save(Vehicle vehicle);

    void deleteById(String id);

    List<Vehicle> findAll();

    Vehicle findById(String id);

    List<Vehicle> findByOwnerId(String ownerId);
}
