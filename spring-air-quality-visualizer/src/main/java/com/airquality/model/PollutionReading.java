package com.airquality.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pollution_readings", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "city", "timestamp" })
})
public class PollutionReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private LocalDate timestamp;

    @Column(nullable = false)
    private Double pm25;

    @Column(nullable = false)
    private Double pm10;

    @Column(nullable = false)
    private Double no2;

    @Column(nullable = false)
    private Double so2;

    @Column(nullable = false)
    private Double co;

    @Column(nullable = false)
    private Integer aqi;

    // Constructors
    public PollutionReading() {
    }

    public PollutionReading(String city, LocalDate timestamp, Double pm25, Double pm10,
            Double no2, Double so2, Double co, Integer aqi) {
        this.city = city;
        this.timestamp = timestamp;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.no2 = no2;
        this.so2 = so2;
        this.co = co;
        this.aqi = aqi;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Double getNo2() {
        return no2;
    }

    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    public Double getSo2() {
        return so2;
    }

    public void setSo2(Double so2) {
        this.so2 = so2;
    }

    public Double getCo() {
        return co;
    }

    public void setCo(Double co) {
        this.co = co;
    }

    public Integer getAqi() {
        return aqi;
    }

    public void setAqi(Integer aqi) {
        this.aqi = aqi;
    }

    @Override
    public String toString() {
        return "PollutionReading{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", timestamp=" + timestamp +
                ", pm25=" + pm25 +
                ", pm10=" + pm10 +
                ", no2=" + no2 +
                ", so2=" + so2 +
                ", co=" + co +
                ", aqi=" + aqi +
                '}';
    }
}
