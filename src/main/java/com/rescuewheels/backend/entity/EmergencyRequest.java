package com.rescuewheels.backend.entity;

import com.rescuewheels.backend.entity.common.Coordinate;
import com.rescuewheels.backend.enums.EmergencyRequestState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "emergency_requests")
public class EmergencyRequest {
    @Id
    private String id;

    private String requestedBy;

    private String responderId;

    private String vehicleId;

    private String state = EmergencyRequestState.PENDING.getState();

    private Coordinate coordinate;

    private String type;

    private Coordinate dropOffCoordinate;

    private boolean isUserRated;

    private boolean isResponderRated;

    public EmergencyRequest() {
    }

    public EmergencyRequest(String requestedBy, String vehicleId, Coordinate coordinate, String type,
                            Coordinate dropOffCoordinate) {
        this.requestedBy = requestedBy;
        this.vehicleId = vehicleId;
        this.coordinate = coordinate;
        this.type = type;
        this.dropOffCoordinate = dropOffCoordinate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Coordinate getDropOffCoordinate() {
        return dropOffCoordinate;
    }

    public void setDropOffCoordinate(Coordinate dropOffCoordinate) {
        this.dropOffCoordinate = dropOffCoordinate;
    }

    public boolean isUserRated() {
        return isUserRated;
    }

    public void setUserRated(boolean userRated) {
        isUserRated = userRated;
    }

    public boolean isResponderRated() {
        return isResponderRated;
    }

    public void setResponderRated(boolean responderRated) {
        isResponderRated = responderRated;
    }

    @Override
    public String toString() {
        return "EmergencyRequest{" +
                "id='" + id + '\'' +
                ", requestedBy='" + requestedBy + '\'' +
                ", responderId='" + responderId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", state='" + state + '\'' +
                ", coordinate=" + coordinate +
                ", type='" + type + '\'' +
                ", dropOffCoordinate=" + dropOffCoordinate +
                ", isUserRated=" + isUserRated +
                ", isResponderRated=" + isResponderRated +
                '}';
    }
}
