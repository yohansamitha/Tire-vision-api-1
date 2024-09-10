package com.tire_vision_api_1.repository;

import com.tire_vision_api_1.utils.mapping.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    @Query(value = "SELECT * FROM Recommendation WHERE range_no = ?1 ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Recommendation findRandomByRangeNo(int rangeNo);
}
