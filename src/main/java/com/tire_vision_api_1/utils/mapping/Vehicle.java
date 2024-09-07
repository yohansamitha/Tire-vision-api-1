package com.tire_vision_api_1.utils.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vehicleId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String brand;

    private String model;

    private String year;

    private String registrationNumber;

    @CreationTimestamp
    private Date registerTime;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;
}
