package com.tire_vision_api_1.services.tireChecks.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tire_vision_api_1.dto.reponses.KeyValueDTO;
import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.tire.TireCheckUpDTO;
import com.tire_vision_api_1.dto.tire.TireCheckUpTableDTO;
import com.tire_vision_api_1.repository.RecommendationRepository;
import com.tire_vision_api_1.repository.TireCheckUpsRepository;
import com.tire_vision_api_1.repository.UserRepository;
import com.tire_vision_api_1.repository.VehicleRepository;
import com.tire_vision_api_1.services.APIServices.TireCheckUpCallService;
import com.tire_vision_api_1.services.tireChecks.TireCheckUpService;
import com.tire_vision_api_1.utils.functions.CommonFunctions;
import com.tire_vision_api_1.utils.mapping.Recommendation;
import com.tire_vision_api_1.utils.mapping.TireCheckup;
import com.tire_vision_api_1.utils.mapping.User;
import com.tire_vision_api_1.utils.mapping.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Service
public class TireCheckUpServiceImpl implements TireCheckUpService {

    @Autowired
    private TireCheckUpCallService checkUpCallService;
    @Autowired
    private TireCheckUpsRepository tireCheckUpsRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${ml-url}")
    private String mlUrl;
    @Autowired
    private RecommendationRepository recommendationRepository;


    @Override
    public StandardResponse insert(TireCheckUpDTO tireCheckUpDTO) {
        StandardResponse response = new StandardResponse();

        // Check if the user exists
        Optional<User> userOptional = userRepository.findById(Long.valueOf(tireCheckUpDTO.getUserId()));
        if (!userOptional.isPresent()) {
            response.setCode("400");
            response.setMessage("User not found. Please provide a valid user ID.");
            return response;
        }

        // Check if the vehicle exists
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(Long.valueOf(tireCheckUpDTO.getVehicleId()));
        if (!vehicleOptional.isPresent()) {
            response.setCode("400");
            response.setMessage("Vehicle not found. Please provide a valid vehicle ID.");
            return response;
        }

        // Start asynchronous image analysis requests
        CompletableFuture<String> flFuture = sendImageRequest(tireCheckUpDTO.getFlImage(), mlUrl);
        CompletableFuture<String> frFuture = sendImageRequest(tireCheckUpDTO.getFrImage(), mlUrl);
        CompletableFuture<String> rlFuture = sendImageRequest(tireCheckUpDTO.getRlImage(), mlUrl);
        CompletableFuture<String> rrFuture = sendImageRequest(tireCheckUpDTO.getRrImage(), mlUrl);

        // Wait for all futures to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(flFuture, frFuture, rlFuture, rrFuture);
        try {
            allFutures.join(); // This will block until all futures complete
            if (hasError(flFuture) || hasError(frFuture) || hasError(rlFuture) || hasError(rrFuture)) {
                response.setCode("400");
                response.setMessage("Failed to process one or more images.");
                return response;
            }
        } catch (CompletionException | CancellationException | InterruptedException | ExecutionException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt(); // Properly handle thread interruption
            }
            response.setCode("500");
            response.setMessage("An error occurred while processing images: " + e.getMessage());
            return response;
        }

        // Parse image analysis results
        Integer flResult = null;
        Integer frResult = null;
        Integer rlResult = null;
        Integer rrResult = null;
        try {
            flResult = parseResult(handleFutureResult(flFuture));
            frResult = parseResult(handleFutureResult(frFuture));
            rlResult = parseResult(handleFutureResult(rlFuture));
            rrResult = parseResult(handleFutureResult(rrFuture));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Set results to the DTO
        tireCheckUpDTO.setFlResults(flResult != null ? String.valueOf(flResult) : "No Image Uploaded");
        tireCheckUpDTO.setFrResults(frResult != null ? String.valueOf(frResult) : "No Image Uploaded");
        tireCheckUpDTO.setRlResults(rlResult != null ? String.valueOf(rlResult) : "No Image Uploaded");
        tireCheckUpDTO.setRrResults(rrResult != null ? String.valueOf(rrResult) : "No Image Uploaded");

        // Calculate the average result
        List<Integer> results = Arrays.asList(flResult, frResult, rlResult, rrResult);
        double avgResult = results.stream()
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(Double.NaN);

        // Handle the case where no valid results are available
        if (Double.isNaN(avgResult)) {
            response.setCode("400");
            response.setMessage("No valid image results available for processing.");
            return response;
        }

        // Determine the appropriate range number based on the average result
        int rangeNo;
        if (avgResult < 50) {
            rangeNo = 3;
        } else if (avgResult <= 80) {
            rangeNo = 2;
        } else {
            rangeNo = 1;
        }

        // Fetch a random recommendation based on the range number
        Recommendation recommendation = recommendationRepository.findRandomByRangeNo(rangeNo);

        // Prepare the new TireCheckup entity
        TireCheckup tireCheckup = new TireCheckup();
        if (recommendation != null) {
            tireCheckUpDTO.setRecommendation(recommendation.getDescription());
            tireCheckup.setRecommendation(recommendation);
        }

        // Set the remaining properties of the TireCheckup entity
        tireCheckup.setUser(userOptional.get());
        tireCheckup.setVehicle(vehicleOptional.get());
        tireCheckup.setFlImage(CommonFunctions.getDecodedImage(tireCheckUpDTO.getFlImage()));
        tireCheckup.setFrImage(CommonFunctions.getDecodedImage(tireCheckUpDTO.getFrImage()));
        tireCheckup.setRlImage(CommonFunctions.getDecodedImage(tireCheckUpDTO.getRlImage()));
        tireCheckup.setRrImage(CommonFunctions.getDecodedImage(tireCheckUpDTO.getRrImage()));
        tireCheckup.setFlResults(tireCheckUpDTO.getFlResults());
        tireCheckup.setFrResults(tireCheckUpDTO.getFrResults());
        tireCheckup.setRlResults(tireCheckUpDTO.getRlResults());
        tireCheckup.setRrResults(tireCheckUpDTO.getRrResults());

        // Save the TireCheckup entity to the database
        tireCheckUpsRepository.save(tireCheckup);

        tireCheckUpDTO.setFlImage("");
        tireCheckUpDTO.setFrImage("");
        tireCheckUpDTO.setRlImage("");
        tireCheckUpDTO.setRrImage("");

        // Build and return the response
        response.setCode("200");
        response.setMessage("Vehicle check-up successful!");
        response.setData(Collections.singletonList(new KeyValueDTO<>("results", tireCheckUpDTO)));
        return response;
    }

    // Utility method to parse a result or return null if "No Image Uploaded"
    private Integer parseResult(String result) {
        return "No Image Uploaded".equals(result) ? null : Integer.parseInt(result);
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

    @Override
    public StandardResponse getTireCheckList(String option, String userId) {
        StandardResponse standardResponse = new StandardResponse();
        if (userId != null && !userId.isEmpty() && !userId.isBlank()) {
            List<TireCheckUpTableDTO> tireCheckUpTableDTOS = new ArrayList<>();
            if (option.equals("1")) {
                Pageable topThree = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "registerTime"));
                tireCheckUpTableDTOS = tireCheckUpsRepository.getLastThreeTireCheckUps(Long.valueOf(userId), topThree).getContent();
            } else if (option.equals("2")) {
                tireCheckUpTableDTOS = tireCheckUpsRepository.getAllTireCheckUps(Long.valueOf(userId));
            }
            KeyValueDTO<List<TireCheckUpTableDTO>> vehicleTableDTOList = new KeyValueDTO<>("tirecheckslist", tireCheckUpTableDTOS);
            KeyValueDTO<Integer> listSize = new KeyValueDTO<>("listsize", tireCheckUpTableDTOS.size());
            ArrayList<KeyValueDTO> keyValueDTOs = new ArrayList<>();
            keyValueDTOs.add(vehicleTableDTOList);
            keyValueDTOs.add(listSize);
            standardResponse.setData(keyValueDTOs);
        }
        return standardResponse;
    }

    @Override
    public StandardResponse update(TireCheckUpDTO u) {
        return null;
    }

    @Override
    public StandardResponse findById(Long aLong) {
        return null;
    }

    @Override
    public StandardResponse deleteById(Long aLong) {
        return null;
    }
}
