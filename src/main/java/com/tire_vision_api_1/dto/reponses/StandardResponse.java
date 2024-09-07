package com.tire_vision_api_1.dto.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse {
    private String code;
    private String message;
    private List<KeyValueDTO> data;
}
