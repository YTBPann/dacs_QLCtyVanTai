package com.ytbpann.quanlyvantai.driver.dto;

import java.time.LocalDate;

public class DriverCreateRequest {

    private String driverCode;
    private String fullName;
    private String phoneNumber;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private String address;
    private String notes;
    private boolean active = true;
    private Long linkedUserAccountId;

    public DriverCreateRequest() {
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseExpiryDate() {
        return licenseExpiryDate;
    }

    public void setLicenseExpiryDate(LocalDate licenseExpiryDate) {
        this.licenseExpiryDate = licenseExpiryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getLinkedUserAccountId() {
        return linkedUserAccountId;
    }

    public void setLinkedUserAccountId(Long linkedUserAccountId) {
        this.linkedUserAccountId = linkedUserAccountId;
    }
}