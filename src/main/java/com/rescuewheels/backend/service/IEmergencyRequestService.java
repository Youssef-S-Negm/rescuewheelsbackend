package com.rescuewheels.backend.service;

import com.rescuewheels.backend.entity.EmergencyRequest;
import com.rescuewheels.backend.entity.common.Coordinate;

import java.util.List;

public interface IEmergencyRequestService {

    EmergencyRequest addRequest(EmergencyRequest emergencyRequest);

    List<EmergencyRequest> findAll();

    EmergencyRequest findById(String id);

    List<EmergencyRequest> getNearbyRequests(Coordinate coordinate);

    EmergencyRequest acceptRequest(String id);

    EmergencyRequest cancelRequest(String id);

    EmergencyRequest cancelResponder(String id);

    EmergencyRequest markInProgress(String id);

    EmergencyRequest markComplete(String id);

    EmergencyRequest rateUser(String id, double rating);

    double getEstimatedPrice(String type, String vehicleId, Coordinate startCoordinate, Coordinate endCoordinate);

    List<EmergencyRequest> findByUserId(String userId);
}
