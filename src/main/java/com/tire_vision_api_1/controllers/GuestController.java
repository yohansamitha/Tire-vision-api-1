package com.tire_vision_api_1.controllers;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.tire.TireCheckUpDTO;
import com.tire_vision_api_1.services.guest.GuestTireCheckUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@RestController
@RequestMapping("/api/v1/guest")
public class GuestController {

    @Autowired
    private GuestTireCheckUpService tireCheckUpService;

    @PostMapping(path = "tire-check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> newTireCheckUp(@RequestBody TireCheckUpDTO tireCheckUpDTO) {
        StandardResponse standardResponse = tireCheckUpService.check(tireCheckUpDTO);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }
}
