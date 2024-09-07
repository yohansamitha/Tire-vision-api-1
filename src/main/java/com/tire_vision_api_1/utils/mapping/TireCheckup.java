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
@Table(name = "TireCheckups")
@AllArgsConstructor
@NoArgsConstructor
public class TireCheckup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] flImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] frImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] rlImage;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] rrImage;

    private String flResults;

    private String frResults;

    private String rlResults;

    private String rrResults;


    @Column(name = "register_time", nullable = false, updatable = false)
    @CreationTimestamp
    private Date registerTime;

    @ManyToOne
    @JoinColumn(name = "recommendationId")
    private Recommendation recommendation;
}
