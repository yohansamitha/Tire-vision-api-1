package com.tire_vision_api_1.utils.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Data
@Entity
@Table(name = "recommendation")
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private int rangeNo;

    @OneToMany(mappedBy = "recommendation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TireCheckup> tireCheckups = new ArrayList<>();

    public Recommendation(String description, int rangeNo) {
        this.description = description;
        this.rangeNo = rangeNo;
    }
}

