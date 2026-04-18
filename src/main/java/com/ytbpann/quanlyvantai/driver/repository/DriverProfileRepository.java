package com.ytbpann.quanlyvantai.driver.repository;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    List<DriverProfile> findAllByOrderByIdDesc();

    boolean existsByDriverCode(String driverCode);

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<DriverProfile> findByDriverCode(String driverCode);
}