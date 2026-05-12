package com.ytbpann.quanlyvantai.vehicle.service;

import com.ytbpann.quanlyvantai.vehicle.dto.VehicleCreateRequest;
import com.ytbpann.quanlyvantai.vehicle.dto.VehicleUpdateRequest;
import com.ytbpann.quanlyvantai.vehicle.entity.Vehicle;
import com.ytbpann.quanlyvantai.vehicle.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByActiveTrueOrderByLicensePlateAsc();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe có ID: " + id));
    }

    @Transactional
    public Vehicle create(VehicleCreateRequest request) {
        String licensePlate = normalizeLicensePlate(request.getLicensePlate());

        if (vehicleRepository.existsByLicensePlateIgnoreCase(licensePlate)) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        vehicle.setVehicleType(normalizeText(request.getVehicleType()));
        vehicle.setCapacity(request.getCapacity());
        vehicle.setActive(request.isActive());
        vehicle.setNotes(normalizeNullableText(request.getNotes()));

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle update(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = findById(id);

        String licensePlate = normalizeLicensePlate(request.getLicensePlate());

        if (vehicleRepository.existsByLicensePlateIgnoreCaseAndIdNot(licensePlate, id)) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại");
        }

        vehicle.setLicensePlate(licensePlate);
        vehicle.setVehicleType(normalizeText(request.getVehicleType()));
        vehicle.setCapacity(request.getCapacity());
        vehicle.setActive(request.isActive());
        vehicle.setNotes(normalizeNullableText(request.getNotes()));

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void toggleActive(Long id) {
        Vehicle vehicle = findById(id);
        vehicle.setActive(!vehicle.isActive());
        vehicleRepository.save(vehicle);
    }

    private String normalizeLicensePlate(String value) {
        return normalizeText(value).toUpperCase();
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private String normalizeNullableText(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}