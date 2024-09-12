package com.tire_vision_api_1.repository;

import com.tire_vision_api_1.dto.tire.TireCheckUpTableDTO;
import com.tire_vision_api_1.utils.mapping.TireCheckup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Repository
public interface TireCheckUpsRepository extends JpaRepository<TireCheckup, Long> {

    @Query("SELECT new com.tire_vision_api_1.dto.tire.TireCheckUpTableDTO(tc.id, tc.vehicle.vehicleId,tc.vehicle.brand,tc.vehicle.model, tc.vehicle.image, tc.user.userId," +
            "tc.flResults, tc.frResults, tc.rlResults, tc.rrResults, tc.recommendation.description, tc.registerTime) " +
            "FROM TireCheckup tc " +
            "WHERE tc.user.userId = :userId " +
            "ORDER BY " +
            "tc.registerTime DESC")
    Page<TireCheckUpTableDTO> getLastThreeTireCheckUps(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.tire_vision_api_1.dto.tire.TireCheckUpTableDTO(tc.id, tc.vehicle.vehicleId,tc.vehicle.brand,tc.vehicle.model, tc.vehicle.image, tc.user.userId," +
            "tc.flResults, tc.frResults, tc.rlResults, tc.rrResults, tc.recommendation.description, tc.registerTime) " +
            "FROM TireCheckup tc " +
            "WHERE tc.user.userId = :userId " +
            "ORDER BY " +
            "tc.registerTime DESC")
    ArrayList<TireCheckUpTableDTO> getAllTireCheckUps(@Param("userId") Long userId);

    @Query("SELECT new com.tire_vision_api_1.dto.tire.TireCheckUpTableDTO(tc.id, tc.vehicle.vehicleId,tc.vehicle.brand,tc.vehicle.model, tc.vehicle.image, tc.user.userId," +
            "tc.flResults, tc.frResults, tc.rlResults, tc.rrResults, tc.recommendation.description, tc.registerTime) " +
            "FROM TireCheckup tc " +
            "WHERE tc.user.userId = :userId AND tc.vehicle.vehicleId = :vehicleId " +
            "ORDER BY " +
            "tc.registerTime DESC")
    Page<TireCheckUpTableDTO> getLastTireCheckUps(Long userId, Long vehicleId, Pageable topThree);

}
