package com.tire_vision_api_1.dto.vehicle;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@NoArgsConstructor
public class SelectVehicleDTO {
    private String vehicleId;
    private String vehicleName;

    public SelectVehicleDTO(Long vehicleId, String brand, String model) {
        this.vehicleId = String.valueOf(vehicleId);
        this.vehicleName = brand + " " + model;
    }
}
