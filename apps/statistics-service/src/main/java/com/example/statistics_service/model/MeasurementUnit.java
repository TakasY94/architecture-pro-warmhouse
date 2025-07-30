package com.example.statistics_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "measurement_units")
@Data
public class MeasurementUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
