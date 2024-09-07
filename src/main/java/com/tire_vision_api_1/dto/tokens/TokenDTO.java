package com.tire_vision_api_1.dto.tokens;

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
public class TokenDTO {
    private String token;
    private String userName;
    private String userAvatar;
    private String userID;
    private String userEmail;
}
