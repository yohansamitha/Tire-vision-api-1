package com.tire_vision_api_1.repository;

import com.tire_vision_api_1.utils.mapping.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT U FROM User U WHERE U.email = :userName")
    Optional<User> findByUsername(@Param("userName") String userName);

    @Query("SELECT U FROM User U WHERE U.email = :userName")
    User getUserByEmail(@Param("userName") String userName);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")
    void resetPassword(String email, String newPassword);
}
