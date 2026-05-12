package com.ytbpann.quanlyvantai.location.service;

import com.ytbpann.quanlyvantai.location.dto.LocationForm;
import com.ytbpann.quanlyvantai.location.entity.LocationPoint;
import com.ytbpann.quanlyvantai.location.repository.LocationPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationManagementService {

    private final LocationPointRepository locationPointRepository;

    public LocationManagementService(LocationPointRepository locationPointRepository) {
        this.locationPointRepository = locationPointRepository;
    }

    @Transactional(readOnly = true)
    public List<LocationPoint> findAll() {
        return locationPointRepository.findAllByOrderByActiveDescCodeAscNameAsc();
    }

    @Transactional(readOnly = true)
    public LocationPoint findById(Long id) {
        return locationPointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy địa điểm có ID: " + id));
    }

    @Transactional
    public LocationPoint create(LocationForm form) {
        validateCodeForCreate(form.getCode());

        LocationPoint locationPoint = new LocationPoint();
        applyForm(locationPoint, form);

        locationPoint.activate();

        return locationPointRepository.save(locationPoint);
    }

    @Transactional
    public LocationPoint update(Long id, LocationForm form) {
        LocationPoint locationPoint = findById(id);

        validateCodeForUpdate(form.getCode(), id);
        applyForm(locationPoint, form);

        return locationPointRepository.save(locationPoint);
    }

    @Transactional
    public void toggleActive(Long id) {
        LocationPoint locationPoint = findById(id);
        locationPoint.toggleActive();
        locationPointRepository.save(locationPoint);
    }

    private void applyForm(LocationPoint locationPoint, LocationForm form) {
        locationPoint.setCode(form.getCode());
        locationPoint.setName(form.getName());
        locationPoint.setType(form.getType());
        locationPoint.setAddress(form.getAddress());
        locationPoint.setProvince(form.getProvince());
        locationPoint.setLatitude(form.getLatitude());
        locationPoint.setLongitude(form.getLongitude());
        locationPoint.setNote(form.getNote());
    }

    private void validateCodeForCreate(String code) {
        if (locationPointRepository.existsByCodeIgnoreCase(code)) {
            throw new IllegalArgumentException("Mã địa điểm đã tồn tại: " + code);
        }
    }

    private void validateCodeForUpdate(String code, Long id) {
        if (locationPointRepository.existsByCodeIgnoreCaseAndIdNot(code, id)) {
            throw new IllegalArgumentException("Mã địa điểm đã tồn tại: " + code);
        }
    }
}