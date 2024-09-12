package com.tire_vision_api_1.controllers;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.vehicle.VehicleDTO;
import com.tire_vision_api_1.services.vehicle.VehicleService;
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
@RequestMapping("/api/v1/vehicle")
@Validated
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping(path = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> registerVehicle(@Validated @RequestBody VehicleDTO vehicleDTO) {
        StandardResponse standardResponse = vehicleService.insert(vehicleDTO);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/list", params = {"option", "userId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> getVehicleList(@RequestParam("option") String option,
                                                           @RequestParam("userId") String userId) {
        StandardResponse standardResponse = vehicleService.getVehicleList(option, userId);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/user-vehicle-list", params = {"userId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> getUserVehicleList(@RequestParam("userId") String userId) {
        StandardResponse standardResponse = vehicleService.getUserVehicleList(userId);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }
}
