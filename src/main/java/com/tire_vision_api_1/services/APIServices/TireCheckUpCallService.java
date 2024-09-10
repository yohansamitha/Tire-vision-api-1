package com.tire_vision_api_1.services.APIServices;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Service
public class TireCheckUpCallService {

    @Async
    public CompletableFuture<String> sendRequest(String imageData, String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"imageData\":\"" + imageData + "\"}"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return CompletableFuture.completedFuture(response.body());
        } catch (Exception e) {
            return CompletableFuture.completedFuture("Error: " + e.getMessage());
        }
    }
}
