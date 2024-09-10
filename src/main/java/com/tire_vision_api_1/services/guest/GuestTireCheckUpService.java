package com.tire_vision_api_1.services.guest;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.tire.TireCheckUpDTO;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public interface GuestTireCheckUpService {
    StandardResponse check(TireCheckUpDTO tireCheckUpDTO);
}
