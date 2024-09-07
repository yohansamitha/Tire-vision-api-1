package com.tire_vision_api_1.utils.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@Entity
@Table(name = "VerificationCode")
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String email;

    private String verificationCode;
}
