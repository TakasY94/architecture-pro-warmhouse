package com.example.statistics_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "devices")
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "device_type_id")
    private Long deviceTypeId;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "created_at")
    private java.time.OffsetDateTime createdAt;
}
