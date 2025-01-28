package com.rescuewheels.backend.rest;

import com.rescuewheels.backend.dto.VehicleRegistrationDto;
import com.rescuewheels.backend.entity.Vehicle;
import com.rescuewheels.backend.service.IVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final IVehicleService vehicleService;

    @Autowired
    public VehicleController(IVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> findAll() {
        List<Vehicle> vehicles = vehicleService.findAll();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable("id") String id) {
        Vehicle vehicle = vehicleService.findById(id);
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> getByUserId(@PathVariable("userId") String userId) {
        List<Vehicle> vehicles = vehicleService.findByOwnerId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping
    public ResponseEntity<Vehicle> saveVehicle(@RequestBody VehicleRegistrationDto input) {
        Vehicle vehicle = new Vehicle(
                input.getMake(),
                input.getModel(),
                input.getType(),
                input.getEnergySource(),
                input.getLicensePlate(),
                input.getYear()
        );

        return new ResponseEntity<>(vehicleService.save(vehicle), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deleteVehicleById(@PathVariable("id") String id) {
        Vehicle vehicle = vehicleService.deleteById(id);
        return ResponseEntity.ok(vehicle);
    }
}

