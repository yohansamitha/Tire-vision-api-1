package com.tire_vision_api_1.services.tireChecks;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.tire.TireCheckUpDTO;
import com.tire_vision_api_1.services.AbstractService;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public interface TireCheckUpService extends AbstractService<TireCheckUpDTO, Long> {
    StandardResponse getTireCheckList(String option, String userId);

    StandardResponse getLastTireCheckForUserVehicle(String vehicleId, String userId);
}
