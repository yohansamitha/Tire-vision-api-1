package com.tire_vision_api_1.dto.tire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TireCheckUpDTO {
    private String vehicleId;
    private String userId;

    private String flImage;
    private String frImage;
    private String rlImage;
    private String rrImage;

    private String flResults;
    private String frResults;
    private String rlResults;
    private String rrResults;

    private String recommendation;
}
