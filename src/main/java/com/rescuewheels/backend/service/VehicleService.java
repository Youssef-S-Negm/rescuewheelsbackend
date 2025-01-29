package com.rescuewheels.backend.service;

import com.rescuewheels.backend.dao.VehicleRepository;
import com.rescuewheels.backend.entity.User;
import com.rescuewheels.backend.entity.Vehicle;
import com.rescuewheels.backend.exception.NotUserVehicleException;
import com.rescuewheels.backend.exception.VehicleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        vehicle.setOwnerId(user.getId());

        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<Vehicle> result = vehicleRepository.findById(id);

        if (result.isEmpty()) {
            throw new VehicleNotFoundException("Vehicle id - " + id + " not found");
        }

        Vehicle vehicle = result.get();

        if (!vehicle.getOwnerId().equals(user.getId()) && !user.getRoles().contains("ROLE_ADMIN")) {
            throw new NotUserVehicleException("Vehicle id - " + id + " doesn't belong to user id - " + user.getId());
        }

        vehicleRepository.deleteById(id);
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle findById(String id) {
        Optional<Vehicle> result = vehicleRepository.findById(id);

        if (result.isEmpty()) {
            throw new VehicleNotFoundException("Vehicle id - " + id + " not found");
        }

        return result.get();
    }

    @Override
    public List<Vehicle> findByOwnerId(String ownerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (!ownerId.equals(user.getId()) && !user.getRoles().contains("ROLE_ADMIN")) {
            throw new NotUserVehicleException("User id - " + user.getId() +
                    " doesn't have permission to view user id - " + ownerId + " vehicles");
        }

        return vehicleRepository.findByOwnerId(ownerId);
    }
}
