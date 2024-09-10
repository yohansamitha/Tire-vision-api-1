package com.tire_vision_api_1.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {

    private String vehicleId;

    @NotNull(message = "UserId must not be Null")
    private String userId;

    @NotNull(message = "Brand must not be Null")
    private String brand;

    @NotNull(message = "Model must not be Null")
    private String model;

    @NotNull(message = "Year must not be Null")
    private String year;

    @NotNull(message = "Vehicle Number must not be Null")
    private String registrationNumber;

    private String image;
}
