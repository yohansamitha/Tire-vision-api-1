package com.tire_vision_api_1.dto.tire;

import com.tire_vision_api_1.utils.functions.CommonFunctions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;


/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@NoArgsConstructor
public class TireCheckUpTableDTO {

    private String id;
    private String vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleImage;
    private String userId;
    private String flResults;
    private String frResults;
    private String rlResults;
    private String rrResults;
    private String recommendation;
    private String checkDate;

    public TireCheckUpTableDTO(
            Long id,
            Long vehicleId,
            String vehicleBrand,
            String vehicleModel,
            byte[] vehicleImage,
            Long userId,
            String flResults, String frResults, String rlResults, String rrResults, String recommendation, Date checkDate) {
        this.id = String.valueOf(id);
        this.vehicleId = String.valueOf(vehicleId);
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleImage = CommonFunctions.getStringImage(vehicleImage);
        this.userId = String.valueOf(userId);
        this.flResults = flResults;
        this.frResults = frResults;
        this.rlResults = rlResults;
        this.rrResults = rrResults;
        this.recommendation = recommendation;
        this.checkDate = new SimpleDateFormat("yyyy-MM-dd").format(checkDate);
    }
}
