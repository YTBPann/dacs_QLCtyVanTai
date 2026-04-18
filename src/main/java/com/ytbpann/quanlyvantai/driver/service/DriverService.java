package com.ytbpann.quanlyvantai.driver.service;

import com.ytbpann.quanlyvantai.driver.dto.DriverCreateRequest;
import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.driver.repository.DriverProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverService {

    private final DriverProfileRepository driverProfileRepository;

    public DriverService(DriverProfileRepository driverProfileRepository) {
        this.driverProfileRepository = driverProfileRepository;
    }

    @Transactional(readOnly = true)
    public List<DriverProfile> getAllDrivers() {
        return driverProfileRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public void createDriver(DriverCreateRequest request) {
        String driverCode = safeTrim(request.getDriverCode());
        String fullName = safeTrim(request.getFullName());
        String phoneNumber = safeTrim(request.getPhoneNumber());
        String licenseNumber = safeTrim(request.getLicenseNumber());
        String address = safeTrim(request.getAddress());
        String notes = safeTrim(request.getNotes());

        if (driverCode == null || driverCode.isBlank()) {
            throw new IllegalArgumentException("Mã tài xế không được để trống.");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Họ tên tài xế không được để trống.");
        }

        if (licenseNumber == null || licenseNumber.isBlank()) {
            throw new IllegalArgumentException("Số GPLX không được để trống.");
        }

        if (driverProfileRepository.existsByDriverCode(driverCode)) {
            throw new IllegalArgumentException("Mã tài xế đã tồn tại.");
        }

        if (driverProfileRepository.existsByLicenseNumber(licenseNumber)) {
            throw new IllegalArgumentException("Số GPLX đã tồn tại.");
        }

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setDriverCode(driverCode);
        driverProfile.setFullName(fullName);
        driverProfile.setPhoneNumber(phoneNumber);
        driverProfile.setLicenseNumber(licenseNumber);
        driverProfile.setLicenseExpiryDate(request.getLicenseExpiryDate());
        driverProfile.setAddress(address);
        driverProfile.setNotes(notes);
        driverProfile.setActive(request.isActive());

        driverProfileRepository.save(driverProfile);
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}