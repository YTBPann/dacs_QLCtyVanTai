package com.ytbpann.quanlyvantai.vehicle.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class VehicleCreateRequest {

    @NotBlank(message = "Biển số xe không được để trống")
    @Size(max = 30, message = "Biển số xe không được vượt quá 30 ký tự")
    private String licensePlate;

    @NotBlank(message = "Loại xe không được để trống")
    @Size(max = 100, message = "Loại xe không được vượt quá 100 ký tự")
    private String vehicleType;

    @DecimalMin(value = "0.0", inclusive = false, message = "Tải trọng phải lớn hơn 0")
    private BigDecimal capacity;

    private boolean active = true;

    @Size(max = 1000, message = "Ghi chú không được vượt quá 1000 ký tự")
    private String notes;

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public boolean isActive() {
        return active;
    }

    public String getNotes() {
        return notes;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}