package com.ytbpann.quanlyvantai.vehicle.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(
        name = "vehicles",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vehicle_license_plate", columnNames = "license_plate")
        }
)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "license_plate", nullable = false, length = 30, unique = true)
    private String licensePlate;

    @NotBlank
    @Size(max = 100)
    @Column(name = "vehicle_type", nullable = false, length = 100)
    private String vehicleType;

    @Column(name = "capacity", precision = 12, scale = 2)
    private BigDecimal capacity;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    public Vehicle() {
    }

    public Vehicle(String licensePlate, String vehicleType, BigDecimal capacity, boolean active, String notes) {
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.active = active;
        this.notes = notes;
    }

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        if (licensePlate != null) {
            licensePlate = licensePlate.trim().toUpperCase();
        }

        if (vehicleType != null) {
            vehicleType = vehicleType.trim();
        }

        if (notes != null) {
            notes = notes.trim();
            if (notes.isEmpty()) {
                notes = null;
            }
        }
    }

    public Long getId() {
        return id;
    }

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

    public void setId(Long id) {
        this.id = id;
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