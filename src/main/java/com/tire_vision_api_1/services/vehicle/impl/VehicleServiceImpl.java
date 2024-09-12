package com.tire_vision_api_1.services.vehicle.impl;

import com.tire_vision_api_1.dto.reponses.KeyValueDTO;
import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.vehicle.SelectVehicleDTO;
import com.tire_vision_api_1.dto.vehicle.VehicleDTO;
import com.tire_vision_api_1.dto.vehicle.VehicleTableDTO;
import com.tire_vision_api_1.repository.VehicleRepository;
import com.tire_vision_api_1.services.vehicle.VehicleService;
import com.tire_vision_api_1.utils.functions.CommonFunctions;
import com.tire_vision_api_1.utils.mapping.Vehicle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public StandardResponse insert(VehicleDTO vehicleDTO) {
        StandardResponse response = new StandardResponse();
        if (!vehicleRepository.existsByRegistrationNumber(vehicleDTO.getRegistrationNumber())) {
            Vehicle vehicle = this.mapper.map(vehicleDTO, Vehicle.class);
            vehicle.setImage(CommonFunctions.getDecodedImage(vehicleDTO.getImage()));

            vehicleRepository.save(vehicle);
            response.setCode("200");
            response.setMessage("Vehicle Registration Successfully!");
        } else {
            response.setCode("400");
            response.setMessage("Something Went Wrong. Please Try Again!");
        }
        return response;
    }

    @Override
    public StandardResponse getVehicleList(String option, String userId) {
        StandardResponse standardResponse = new StandardResponse();
        if (userId != null && !userId.isEmpty() && !userId.isBlank()) {
            List<VehicleTableDTO> vehicleTableDTOS = new ArrayList<>();
            if (option.equals("1")) {
                Pageable topThree = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "registerTime"));
                vehicleTableDTOS = vehicleRepository.getLastThreeRegisteredVehicle(Long.valueOf(userId), topThree).getContent();
            } else if (option.equals("2")) {
                vehicleTableDTOS = vehicleRepository.getAllRegisteredVehicle(Long.valueOf(userId));
            }
            KeyValueDTO<List<VehicleTableDTO>> vehicleTableDTOList = new KeyValueDTO<>("vehiclelist", vehicleTableDTOS);
            KeyValueDTO<Integer> listSize = new KeyValueDTO<>("listsize", vehicleTableDTOS.size());
            ArrayList<KeyValueDTO> keyValueDTOs = new ArrayList<>();
            keyValueDTOs.add(vehicleTableDTOList);
            keyValueDTOs.add(listSize);
            standardResponse.setData(keyValueDTOs);
        }
        return standardResponse;
    }

    @Override
    public StandardResponse getUserVehicleList(String userId) {
        StandardResponse standardResponse = new StandardResponse();
        if (userId != null && !userId.isEmpty() && !userId.isBlank()) {
            List<SelectVehicleDTO> vehicleTableDTOS = vehicleRepository.getAllUserVehicle(Long.valueOf(userId));
            KeyValueDTO<List<SelectVehicleDTO>> vehicleTableDTOList = new KeyValueDTO<>("vehiclelist", vehicleTableDTOS);
            ArrayList<KeyValueDTO> keyValueDTOs = new ArrayList<>();
            keyValueDTOs.add(vehicleTableDTOList);
            standardResponse.setData(keyValueDTOs);
        }
        return standardResponse;
    }

    @Override
    public StandardResponse update(VehicleDTO u) {
        return null;
    }

    @Override
    public StandardResponse findById(Long id) {
        return null;
    }

    @Override
    public StandardResponse deleteById(Long id) {
        return null;
    }

}
