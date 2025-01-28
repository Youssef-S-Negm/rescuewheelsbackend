package com.rescuewheels.backend.enums;

public enum VehicleEnergySource {
    PETROL("PETROL"), DIESEL("DIESEL"), ELECTRIC("ELECTRIC"), HYBRID("HYBRID");

    private final String energySource;

    VehicleEnergySource(String energySource) {
        this.energySource = energySource;
    }

    public String getEnergySource() {
        return energySource;
    }
}
