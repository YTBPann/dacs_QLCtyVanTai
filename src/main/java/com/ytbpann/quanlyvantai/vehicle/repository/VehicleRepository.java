package com.ytbpann.quanlyvantai.vehicle.repository;

import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByLicensePlateIgnoreCase(String licensePlate);

    boolean existsByLicensePlateIgnoreCaseAndIdNot(String licensePlate, Long id);

    List<Vehicle> findAllByOrderByIdDesc();

    List<Vehicle> findByActiveTrueOrderByLicensePlateAsc();
}