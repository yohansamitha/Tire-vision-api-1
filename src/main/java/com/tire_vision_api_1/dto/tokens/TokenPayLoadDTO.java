package com.tire_vision_api_1.dto.tokens;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPayLoadDTO {
    private String sessionId;
    private String userName;
    private Date expiresTime;
}
