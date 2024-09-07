package com.tire_vision_api_1.controllers;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.user.UserDTO;
import com.tire_vision_api_1.services.user.UserService;
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
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse> registerUser(@Validated(UserDTO.BasicValidation.class) @RequestBody UserDTO userDTO) {
        StandardResponse standardResponse = userService.insert(userDTO);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<StandardResponse> forgotPassword(@RequestParam("email") String email) {
        StandardResponse standardResponse = userService.initiateForgotPassword(email);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<StandardResponse> resetPassword(@RequestParam("email") String email,
                                                          @RequestParam("verificationCode") String verificationCode,
                                                          @RequestParam("newPassword") String newPassword) {
        StandardResponse standardResponse = userService.resetPassword(email, verificationCode, newPassword);
        return new ResponseEntity<>(standardResponse, HttpStatus.OK);
    }
}
