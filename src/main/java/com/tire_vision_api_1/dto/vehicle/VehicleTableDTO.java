package com.tire_vision_api_1.dto.vehicle;

import com.tire_vision_api_1.utils.functions.CommonFunctions;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@NoArgsConstructor
public class VehicleTableDTO {

    private String vehicleId;

    private String brand;

    private String model;

    private String year;

    private String image;

    public VehicleTableDTO(Long vehicleId, String brand, String model, String year, byte[] image) {
        this.vehicleId = String.valueOf(vehicleId);
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.image = CommonFunctions.getStringImage(image);
    }
}
