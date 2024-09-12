package com.tire_vision_api_1.repository;

import com.tire_vision_api_1.dto.vehicle.SelectVehicleDTO;
import com.tire_vision_api_1.dto.vehicle.VehicleTableDTO;
import com.tire_vision_api_1.utils.mapping.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByRegistrationNumber(String vehicleNumber);

    @Query("SELECT new com.tire_vision_api_1.dto.vehicle.VehicleTableDTO(v.vehicleId, v.brand, v.model, v.year, v.image) " +
            "FROM Vehicle v " +
            "WHERE v.user.userId = :userId " +
            "ORDER BY " +
            "v.registerTime DESC")
    Page<VehicleTableDTO> getLastThreeRegisteredVehicle(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.tire_vision_api_1.dto.vehicle.VehicleTableDTO(v.vehicleId, v.brand, v.model, v.year, v.image) " +
            "FROM Vehicle v " +
            "WHERE v.user.userId = :userId " +
            "ORDER BY " +
            "v.registerTime DESC")
    ArrayList<VehicleTableDTO> getAllRegisteredVehicle(@Param("userId") Long userId);

    @Query("SELECT new com.tire_vision_api_1.dto.vehicle.SelectVehicleDTO(v.vehicleId, v.brand, v.model) " +
            "FROM Vehicle v " +
            "WHERE v.user.userId = :userId " +
            "ORDER BY " +
            "v.registerTime DESC")
    List<SelectVehicleDTO> getAllUserVehicle(@Param("userId") Long userId);
}
