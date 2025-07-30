package com.example.temperature_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

@RestController
public class TemperatureController {

    @GetMapping("/temperature/{location}")
    public ResponseEntity<TemperatureResponse> getTemperature(
            @PathVariable String location) {

        // Генерация случайной температуры от -20 до 40
        Random random = new Random();
        double temperatureValue = -20 + random.nextInt(61); // диапазон -20..40

        // Создание объекта ответа
        TemperatureResponse response = new TemperatureResponse(
                temperatureValue,
                "Celsius",
                ZonedDateTime.now(ZoneId.of("UTC")),
                location,
                "active",
                "sensor-12345",
                "temperature-sensor-v2",
                "Indoor temperature sensor"
        );
        return ResponseEntity.ok(response);
    }

    // POJO для ответа
    private record TemperatureResponse(
            double value,
            String unit,
            ZonedDateTime timestamp,
            String location,
            String status,
            String sensor_id,
            String sensor_type,
            String description
    ) {}
}
