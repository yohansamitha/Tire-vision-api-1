package com.tire_vision_api_1.controllers;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.tire.TireCheckUpDTO;
import com.tire_vision_api_1.services.tireChecks.TireCheckUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@RestController
@RequestMapping("/api/v1/tire")
@Validated
public class TireCheckUpsController {

    @Autowired
    private TireCheckUpService tireCheckUpService;

    @PostMapping(path = "check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> newTireCheckUp(@RequestBody TireCheckUpDTO tireCheckUpDTO) {
        StandardResponse standardResponse = tireCheckUpService.insert(tireCheckUpDTO);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/list", params = {"option", "userId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> getTireCheckList(@RequestParam("option") String option,
                                                             @RequestParam("userId") String userId) {
        StandardResponse standardResponse = tireCheckUpService.getTireCheckList(option, userId);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/last-check", params = {"vehicleId", "userId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> getLastTireCheckForUserVehicle(@RequestParam("vehicleId") String vehicleId,
                                                                           @RequestParam("userId") String userId) {
        StandardResponse standardResponse = tireCheckUpService.getLastTireCheckForUserVehicle(vehicleId, userId);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }
}
