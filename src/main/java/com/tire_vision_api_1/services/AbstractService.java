package com.tire_vision_api_1.services;

import com.tire_vision_api_1.dto.reponses.StandardResponse;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public interface AbstractService<T, ID> {

    StandardResponse insert(T t);

    StandardResponse update(T u);

    StandardResponse findById(ID id);

    StandardResponse deleteById(ID id);
}
