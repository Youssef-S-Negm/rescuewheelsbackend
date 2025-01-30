package com.rescuewheels.backend.service;

import com.rescuewheels.backend.constants.AverageCapacities;
import com.rescuewheels.backend.constants.PricingConstants;
import com.rescuewheels.backend.dao.EmergencyRequestRepository;
import com.rescuewheels.backend.dao.UserRepository;
import com.rescuewheels.backend.dao.VehicleRepository;
import com.rescuewheels.backend.entity.EmergencyRequest;
import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.entity.Vehicle;
import com.rescuewheels.backend.entity.common.Coordinate;
import com.rescuewheels.backend.enums.EmergencyRequestState;
import com.rescuewheels.backend.enums.EmergencyRequestType;
import com.rescuewheels.backend.enums.UserRoles;
import com.rescuewheels.backend.enums.VehicleEnergySource;
import com.rescuewheels.backend.exception.EntityNotFoundException;
import com.rescuewheels.backend.exception.ForbiddenOperationException;
import com.rescuewheels.backend.util.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class
EmergencyRequestService implements IEmergencyRequestService {

    private final EmergencyRequestRepository emergencyRequestRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public EmergencyRequestService(EmergencyRequestRepository emergencyRequestRepository,
                                   UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public EmergencyRequest addRequest(EmergencyRequest emergencyRequest) {
        Optional<User> userResult = userRepository.findById(emergencyRequest.getRequestedBy());

        if (userResult.isEmpty()) {
            throw new EntityNotFoundException("User id - " + emergencyRequest.getRequestedBy() + " not found");
        }

        EmergencyRequest request = emergencyRequestRepository.save(emergencyRequest);
        User user = userResult.get();

        user.setOnGoingRequestId(request.getId());
        userRepository.save(user);

        return request;
    }

    @Override
    public List<EmergencyRequest> findAll() {
        return emergencyRequestRepository.findAll();
    }

    @Override
    public EmergencyRequest findById(String id) {
        Optional<EmergencyRequest> result = emergencyRequestRepository.findById(id);

        if (result.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        return result.get();
    }

    @Override
    public List<EmergencyRequest> getNearbyRequests(Coordinate coordinate) {
        List<EmergencyRequest> allRequests = emergencyRequestRepository.findAll();

        // Filter requests that are within 5 km of the given coordinate
        return allRequests.stream()
                .filter(request ->
                        Location.calculateDistance(coordinate, request.getCoordinate()) <= 5
                                && request.getState().equals(EmergencyRequestState.PENDING.getState())
                )
                .toList();
    }

    @Override
    @Transactional
    public EmergencyRequest acceptRequest(String id) {
        Optional<EmergencyRequest> emergencyResult = emergencyRequestRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User responder = (User) authentication.getPrincipal();

        if (emergencyResult.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        EmergencyRequest request = emergencyResult.get();

        if (!request.getState().equals(EmergencyRequestState.PENDING.getState())) {
            throw new ForbiddenOperationException("Request id - " + id + " is not pending");
        }

        request.setState(EmergencyRequestState.RESPONDING.getState());
        request.setResponderId(responder.getId());

        responder.setOnGoingRequestId(request.getId());

        userRepository.save(responder);

        return emergencyRequestRepository.save(request);
    }

    @Override
    @Transactional
    public EmergencyRequest cancelRequest(String id) {
        Optional<EmergencyRequest> result = emergencyRequestRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        if (result.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        EmergencyRequest request = result.get();

        if (!authUser.getId().equals(request.getRequestedBy())
                && !authUser.getRoles().contains(UserRoles.ADMIN.getRole())) {

            throw new ForbiddenOperationException("Request id - " + request.getId() +
                    " is not requested by the authenticated user");
        }

        Optional<User> requesterResult = userRepository.findById(request.getRequestedBy());

        if (requesterResult.isEmpty()) {
            throw new EntityNotFoundException("User id - " + request.getRequestedBy() + " not found");
        }

        User requester = requesterResult.get();

        requester.setOnGoingRequestId(null);

        userRepository.save(requester);

        if (request.getResponderId() != null) {
            Optional<User> responderResult = userRepository.findById(request.getResponderId());

            if (responderResult.isEmpty()) {
                throw new EntityNotFoundException("User id - " + request.getRequestedBy() + " not found");
            }

            User responder = responderResult.get();

            responder.setOnGoingRequestId(null);
            userRepository.save(responder);
        }

        request.setState(EmergencyRequestState.CANCELLED.getState());

        return emergencyRequestRepository.save(request);
    }

    @Override
    @Transactional
    public EmergencyRequest cancelResponder(String id) {
        Optional<EmergencyRequest> result = emergencyRequestRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        if (result.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        EmergencyRequest request = result.get();

        if (!authUser.getId().equals(request.getResponderId())
                && !authUser.getRoles().contains(UserRoles.ADMIN.getRole())) {

            throw new ForbiddenOperationException("Authenticated user id - " + authUser.getId() + " is not a responder");

        }

        Optional<User> responderResult = userRepository.findById(request.getResponderId());

        if (responderResult.isEmpty()) {
            throw new EntityNotFoundException("User id - " + request.getResponderId() + " not found");
        }

        User responder = responderResult.get();

        responder.setOnGoingRequestId(null);

        request.setState(EmergencyRequestState.PENDING.getState());
        request.setResponderId(null);

        userRepository.save(responder);

        return emergencyRequestRepository.save(request);
    }

    @Override
    @Transactional
    public EmergencyRequest markInProgress(String id) {
        Optional<EmergencyRequest> result = emergencyRequestRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        if (result.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        EmergencyRequest request = result.get();

        if (!authUser.getId().equals(request.getResponderId())
                && !authUser.getRoles().contains(UserRoles.ADMIN.getRole())) {

            throw new ForbiddenOperationException("Authenticated user id - " + authUser.getId() + " is not a responder");

        }

        request.setState(EmergencyRequestState.IN_PROGRESS.getState());

        return emergencyRequestRepository.save(request);
    }

    @Override
    @Transactional
    public EmergencyRequest markComplete(String id) {
        Optional<EmergencyRequest> result = emergencyRequestRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();

        if (result.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        EmergencyRequest request = result.get();

        if (!authUser.getId().equals(request.getResponderId())
                && !authUser.getRoles().contains(UserRoles.ADMIN.getRole())) {

            throw new ForbiddenOperationException("Authenticated user id - " + authUser.getId() + " is not a responder");

        }

        Optional<User> requesterResult = userRepository.findById(request.getRequestedBy());
        Optional<User> responderResult = userRepository.findById(request.getResponderId());

        if (requesterResult.isEmpty() || responderResult.isEmpty()) {
            throw new EntityNotFoundException("User id - " + request.getRequestedBy() + " not found");
        }

        User requester = requesterResult.get();
        User responder = responderResult.get();

        requester.setOnGoingRequestId(null);
        responder.setOnGoingRequestId(null);

        request.setState(EmergencyRequestState.DONE.getState());

        userRepository.save(requester);
        userRepository.save(responder);

        return emergencyRequestRepository.save(request);
    }

    @Override
    @Transactional
    public EmergencyRequest rateUser(String id, double rating) {
        Optional<EmergencyRequest> result = emergencyRequestRepository.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();

        if (result.isEmpty()) {
            throw new EntityNotFoundException("Emergency request id - " + id + " not found");
        }

        EmergencyRequest request = result.get();

        if (!authenticatedUser.getId().equals(request.getRequestedBy())
                && !authenticatedUser.getId().equals(request.getResponderId())
                && !authenticatedUser.getRoles().contains(UserRoles.ADMIN.getRole())) {

            throw new ForbiddenOperationException(
                    "Authenticated user is neither a requester nor a responder for request id - " + request.getId()
            );
        }

        Optional<User> requesterResult = userRepository.findById(request.getRequestedBy());
        Optional<User> responderResult = userRepository.findById(request.getResponderId());

        if (requesterResult.isEmpty() || responderResult.isEmpty()) {
            throw new EntityNotFoundException("User id - " + request.getRequestedBy() + " not found");
        }

        if (authenticatedUser.getId().equals(request.getRequestedBy())) {
            User responder = responderResult.get();

            responder.setRating(calculateRatingForUser(responder, rating));
            request.setResponderRated(true);

            userRepository.save(responder);
        } else {
            User requester = requesterResult.get();

            requester.setRating(calculateRatingForUser(requester, rating));
            request.setUserRated(true);

            userRepository.save(requester);
        }

        return emergencyRequestRepository.save(request);
    }

    @Override
    public double getEstimatedPrice(String type, String vehicleId, Coordinate startCoordinate,
                                    Coordinate endCoordinate) {

        Optional<Vehicle> vehicleQueryResult = vehicleRepository.findById(vehicleId);

        if (vehicleQueryResult.isEmpty()) {
            throw new EntityNotFoundException("Vehicle id - " + vehicleId + " not found");
        }

        return calculateEstimatedPrice(type, startCoordinate, endCoordinate, vehicleQueryResult.get());
    }

    @Override
    public List<EmergencyRequest> findByUserId(String userId) {
        return emergencyRequestRepository.findByUserId(userId);
    }

    private double calculateRatingForUser(User user, double rating) {
        List<EmergencyRequest> requests = user.getRequests().stream()
                .filter(request -> request.getState().equals(EmergencyRequestState.DONE.getState()))
                .toList();
        double totalRating = user.getRating() * requests.size();

        return Math.round((totalRating + rating) / (requests.size() + 1.0) * 100.0) / 100.0;
    }

    private double calculateEstimatedPrice(String type, Coordinate coordinate,
                                           Coordinate dropOffCoordinate, Vehicle vehicle) {
        double serviceFee = 50;
        EmergencyRequestType emergencyType = EmergencyRequestType.valueOf(type);
        VehicleEnergySource energySource = VehicleEnergySource.valueOf(vehicle.getEnergySource());

        switch (emergencyType) {
            case FLAT_TIRE -> serviceFee += 5;

            case OUT_OF_FUEL_DEAD_BATTERY -> {
                switch (energySource) {
                    case PETROL, HYBRID -> serviceFee += AverageCapacities.FUEL_TANK * PricingConstants.PETROL_PRICE;
                    case DIESEL -> serviceFee += AverageCapacities.FUEL_TANK * PricingConstants.DIESEL_PRICE;
                    case ELECTRIC -> serviceFee += AverageCapacities.BATTERY_CAPACITY * PricingConstants.ELECTRIC_PRICE;
                }
            }

            case OTHER -> {
                double distance = Location.calculateDistance(coordinate, dropOffCoordinate);
                serviceFee += distance * PricingConstants.TRANSPORTATION_PRICE;
            }
        }

        return (double) Math.round(serviceFee);
    }
}
