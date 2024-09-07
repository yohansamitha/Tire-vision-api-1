package com.tire_vision_api_1.dto.reponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValueDTO<T> {
    private String key;
    private T value;
}
