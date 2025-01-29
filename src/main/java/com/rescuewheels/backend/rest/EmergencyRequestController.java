package com.rescuewheels.backend.rest;

import com.rescuewheels.backend.dto.GetEstimatedPriceRequestBody;
import com.rescuewheels.backend.entity.EmergencyRequest;
import com.rescuewheels.backend.entity.common.Coordinate;
import com.rescuewheels.backend.service.IEmergencyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emergency-requests")
public class EmergencyRequestController {

    private final IEmergencyRequestService emergencyRequestService;

    @Autowired
    public EmergencyRequestController(IEmergencyRequestService emergencyRequestService) {
        this.emergencyRequestService = emergencyRequestService;
    }

    @PostMapping
    public ResponseEntity<EmergencyRequest> addRequest(@RequestBody EmergencyRequest emergencyRequest) {
        EmergencyRequest savedRequest = emergencyRequestService.addRequest(emergencyRequest);
        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyRequest> getRequestById(@PathVariable("id") String id) {
        EmergencyRequest request = emergencyRequestService.findById(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<EmergencyRequest>> getAllRequests() {
        Iterable<EmergencyRequest> requests = emergencyRequestService.findAll();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/nearby")
    public ResponseEntity<Iterable<EmergencyRequest>> getNearbyRequests(@RequestParam("latitude") double latitude,
                                                                        @RequestParam("longitude") double longitude) {
        Iterable<EmergencyRequest> requests = emergencyRequestService
                .getNearbyRequests(new Coordinate(latitude, longitude));

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/estimated-price/{type}?vehicleId={vehicleId}")
    public ResponseEntity<Double> getEstimatedPrice(@PathVariable("type") String type,
                                                    @RequestParam("vehicleId") String vehicleId,
                                                    @RequestBody GetEstimatedPriceRequestBody body) {

        double estimatedPrice = emergencyRequestService
                .getEstimatedPrice(type, vehicleId, body.getStartCoordinate(), body.getEndCoordinate());

        return new ResponseEntity<>(estimatedPrice, HttpStatus.OK);
    }

    @GetMapping("?userId={userId}")
    public ResponseEntity<List<EmergencyRequest>> getUserHistory(@RequestParam("userId") String userId) {
        List<EmergencyRequest> result = emergencyRequestService.findByUserId(userId);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<EmergencyRequest> acceptRequest(@PathVariable("id") String id) {
        EmergencyRequest request = emergencyRequestService.acceptRequest(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<EmergencyRequest> cancelRequest(@PathVariable("id") String id) {
        EmergencyRequest request = emergencyRequestService.cancelRequest(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/{id}/cancel-responder")
    public ResponseEntity<EmergencyRequest> cancelResponder(@PathVariable("id") String id) {
        EmergencyRequest request = emergencyRequestService.cancelResponder(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/{id}/in-progress")
    public ResponseEntity<EmergencyRequest> markInProgress(@PathVariable("id") String id) {
        EmergencyRequest request = emergencyRequestService.markInProgress(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<EmergencyRequest> markComplete(@PathVariable("id") String id) {
        EmergencyRequest request = emergencyRequestService.markComplete(id);
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    @PutMapping("/{id}?rating={rating}")
    public ResponseEntity<EmergencyRequest> rateUser(@PathVariable("id") String id,
                                      @RequestParam("rating") double rating) {
        EmergencyRequest request = emergencyRequestService.rateUser(id, rating);
        return ResponseEntity.ok(request);
    }
}
