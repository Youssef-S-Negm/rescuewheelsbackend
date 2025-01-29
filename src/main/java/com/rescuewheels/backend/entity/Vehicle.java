package com.rescuewheels.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
public class Vehicle {

    @Id
    private String id;

    private String ownerId;

    private String make;

    private String model;

    private String type;

    private String energySource;

    @Indexed(unique = true)
    private String licensePlate;

    private int year;

    public Vehicle() {
    }

    public Vehicle(
            String make,
            String model,
            String type,
            String energySource,
            String licensePlate,
            int year
    ) {
        this.make = make;
        this.model = model;
        this.type = type;
        this.energySource = energySource;
        this.licensePlate = licensePlate;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnergySource() {
        return energySource;
    }

    public void setEnergySource(String energySource) {
        this.energySource = energySource;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                ", energySource='" + energySource + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", year=" + year +
                '}';
    }
}
