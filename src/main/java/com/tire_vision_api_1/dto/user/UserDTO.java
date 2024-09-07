package com.tire_vision_api_1.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;

    @NotNull(message = "First Name must not be Null", groups = {BasicValidation.class, NameValidation.class})
    @NotBlank(message = "First Name must not be blank", groups = {BasicValidation.class, NameValidation.class})
    private String firstName;

    @NotNull(message = "Last Name must not be Null", groups = {BasicValidation.class, NameValidation.class})
    @NotBlank(message = "Last Name must not be blank", groups = {BasicValidation.class, NameValidation.class})
    private String lastName;

    @Email(message = "Email must be valid", groups = {BasicValidation.class})
    @NotNull(message = "Email must not be Null", groups = {BasicValidation.class})
    @NotBlank(message = "Email must not be blank", groups = {BasicValidation.class})
    private String email;

    @NotNull(message = "Password must not be Null", groups = {BasicValidation.class})
    @NotBlank(message = "Password must not be blank", groups = {BasicValidation.class})
    private String password;

    private String userAvatar;

    public interface BasicValidation {
    }

    public interface NameValidation {
    }
}
