package com.tire_vision_api_1.repository;

import com.tire_vision_api_1.utils.mapping.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmailAndVerificationCode(String email, String verificationCode);

    Optional<VerificationCode> findByUserIdAndEmail(Long userId, String email);

    @Modifying
    @Transactional
    void deleteByVerificationCode(String verificationCode);
}
