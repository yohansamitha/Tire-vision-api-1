package com.tire_vision_api_1.services.vehicle;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.vehicle.VehicleDTO;
import com.tire_vision_api_1.services.AbstractService;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public interface VehicleService extends AbstractService<VehicleDTO, Long> {
    StandardResponse getVehicleList(String option, String userId);

    StandardResponse getUserVehicleList(String userId);
}
