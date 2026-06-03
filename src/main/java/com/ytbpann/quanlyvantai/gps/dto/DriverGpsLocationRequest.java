package com.ytbpann.quanlyvantai.gps.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DriverGpsLocationRequest {

    @NotNull(message = "Vĩ độ không được để trống")
    @DecimalMin(value = "-90.0", message = "Vĩ độ không hợp lệ")
    @DecimalMax(value = "90.0", message = "Vĩ độ không hợp lệ")
    private Double latitude;

    @NotNull(message = "Kinh độ không được để trống")
    @DecimalMin(value = "-180.0", message = "Kinh độ không hợp lệ")
    @DecimalMax(value = "180.0", message = "Kinh độ không hợp lệ")
    private Double longitude;

    private Double accuracyMeter;

    private Double speedMeterPerSecond;

    private Double headingDegree;

    private String source;

    private LocalDateTime recordedAt;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAccuracyMeter() {
        return accuracyMeter;
    }

    public void setAccuracyMeter(Double accuracyMeter) {
        this.accuracyMeter = accuracyMeter;
    }

    public Double getSpeedMeterPerSecond() {
        return speedMeterPerSecond;
    }

    public void setSpeedMeterPerSecond(Double speedMeterPerSecond) {
        this.speedMeterPerSecond = speedMeterPerSecond;
    }

    public Double getHeadingDegree() {
        return headingDegree;
    }

    public void setHeadingDegree(Double headingDegree) {
        this.headingDegree = headingDegree;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
