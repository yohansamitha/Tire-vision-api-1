package com.tire_vision_api_1.utils.functions;

import java.util.Base64;
/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public class CommonFunctions {
    public static String getStringImage(byte[] image) {
        return image == null ? "" : "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(image);
    }

    public static byte[] getDecodedImage(String image) {
        if (image != null && !image.isEmpty() && !image.isBlank()) {
            return Base64.getDecoder().decode(image.split(",")[1]);
        } else {
            return new byte[0];
        }
    }
}
