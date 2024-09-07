package com.tire_vision_api_1.services.user;

import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.user.UserDTO;
import com.tire_vision_api_1.services.AbstractService;
import org.springframework.security.core.userdetails.UserDetailsService;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public interface UserService extends UserDetailsService, AbstractService<UserDTO, Long> {
    StandardResponse initiateForgotPassword(String email);

    StandardResponse resetPassword(String email, String verificationCode, String newPassword);
}
