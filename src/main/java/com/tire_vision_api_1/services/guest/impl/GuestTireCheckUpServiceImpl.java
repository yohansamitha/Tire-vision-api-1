package com.tire_vision_api_1.services.guest.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tire_vision_api_1.dto.reponses.KeyValueDTO;
import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.tire.TireCheckUpDTO;
import com.tire_vision_api_1.services.APIServices.TireCheckUpCallService;
import com.tire_vision_api_1.services.guest.GuestTireCheckUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Service
public class GuestTireCheckUpServiceImpl implements GuestTireCheckUpService {

    @Autowired
    private TireCheckUpCallService checkUpCallService;
    @Value("${ml-url}")
    private String mlUrl;

    @Override
    public StandardResponse check(TireCheckUpDTO tireCheckUpDTO) {
        StandardResponse response = new StandardResponse();

        CompletableFuture<String> flFuture = sendImageRequest(tireCheckUpDTO.getFlImage(), mlUrl);
        CompletableFuture<String> frFuture = sendImageRequest(tireCheckUpDTO.getFrImage(), mlUrl);
        CompletableFuture<String> rlFuture = sendImageRequest(tireCheckUpDTO.getRlImage(), mlUrl);
        CompletableFuture<String> rrFuture = sendImageRequest(tireCheckUpDTO.getRrImage(), mlUrl);

        CompletableFuture.allOf(flFuture, frFuture, rlFuture, rrFuture).join();

        try {
            if (hasError(flFuture) || hasError(frFuture) || hasError(rlFuture) || hasError(rrFuture)) {
                response.setCode("400");
                response.setMessage("Failed to upload one or more images.");
            } else {

                tireCheckUpDTO.setFlResults(handleFutureResult(flFuture));
                tireCheckUpDTO.setFrResults(handleFutureResult(frFuture));
                tireCheckUpDTO.setRlResults(handleFutureResult(rlFuture));
                tireCheckUpDTO.setRrResults(handleFutureResult(rrFuture));

                response.setCode("200");
                response.setMessage("Tire CheckUp Success!");
                response.setData(Collections.singletonList(new KeyValueDTO<TireCheckUpDTO>("results", tireCheckUpDTO)));
            }
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            response.setCode("500");
            response.setMessage("An error occurred while processing requests: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        return response;
    }

    private String getPredictionResults(CompletableFuture<String> flFuture) throws JsonProcessingException, InterruptedException, ExecutionException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(flFuture.get());
        String result = node.get("result").asText();
        System.out.println("Result: " + result);
        return result;
    }

    private CompletableFuture<String> sendImageRequest(String imageData, String url) {
        if (imageData == null || imageData.isEmpty()) {
            return CompletableFuture.completedFuture("No Image");
        } else {
            return checkUpCallService.sendRequest(imageData, url);
        }
    }

    private String handleFutureResult(CompletableFuture<String> future) throws ExecutionException, InterruptedException, JsonProcessingException {
        String result = future.get();
        if ("No Image".equals(result)) {
            return "No Image Uploaded";
        } else {
            return getPredictionResults(future);
        }
    }

    private boolean hasError(CompletableFuture<String> future) throws ExecutionException, InterruptedException {
        return future.get().startsWith("Error: ");
    }
}
