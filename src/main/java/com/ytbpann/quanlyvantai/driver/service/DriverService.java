package com.ytbpann.quanlyvantai.driver.service;

import com.ytbpann.quanlyvantai.driver.dto.DriverCreateRequest;
import com.ytbpann.quanlyvantai.driver.dto.DriverUpdateRequest;
import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.driver.repository.DriverProfileRepository;
import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverService {

    private final DriverProfileRepository driverProfileRepository;
    private final UserAccountRepository userAccountRepository;

    public DriverService(DriverProfileRepository driverProfileRepository,
                         UserAccountRepository userAccountRepository) {
        this.driverProfileRepository = driverProfileRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional(readOnly = true)
    public List<DriverProfile> getAllDrivers() {
        return driverProfileRepository.findAllWithLinkedUserAccountOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public DriverProfile getDriverById(Long driverId) {
        return driverProfileRepository.findByIdWithLinkedUserAccount(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ tài xế."));
    }

    @Transactional(readOnly = true)
    public List<UserAccount> getAvailableDriverAccounts() {
        return userAccountRepository.findByRoleOrderByUsernameAsc(RoleName.DRIVER)
                .stream()
                .filter(userAccount -> !driverProfileRepository.existsByLinkedUserAccount_Id(userAccount.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserAccount> getAvailableDriverAccountsForEdit(Long driverId) {
        DriverProfile driverProfile = getDriverById(driverId);
        Long currentLinkedUserId = driverProfile.getLinkedUserAccount() != null
                ? driverProfile.getLinkedUserAccount().getId()
                : null;

        return userAccountRepository.findByRoleOrderByUsernameAsc(RoleName.DRIVER)
                .stream()
                .filter(userAccount -> {
                    if (currentLinkedUserId != null && currentLinkedUserId.equals(userAccount.getId())) {
                        return true;
                    }
                    return !driverProfileRepository.existsByLinkedUserAccount_Id(userAccount.getId());
                })
                .toList();
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

        UserAccount linkedUserAccount = null;
        if (request.getLinkedUserAccountId() != null) {
            linkedUserAccount = userAccountRepository.findById(request.getLinkedUserAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Tài khoản DRIVER được chọn không tồn tại."));

            if (linkedUserAccount.getRole() != RoleName.DRIVER) {
                throw new IllegalArgumentException("Chỉ được liên kết với tài khoản có role DRIVER.");
            }

            if (driverProfileRepository.existsByLinkedUserAccount_Id(linkedUserAccount.getId())) {
                throw new IllegalArgumentException("Tài khoản DRIVER này đã được gắn với hồ sơ tài xế khác.");
            }
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
        driverProfile.setLinkedUserAccount(linkedUserAccount);

        driverProfileRepository.save(driverProfile);
    }

    @Transactional
    public void updateDriver(Long driverId, DriverUpdateRequest request) {
        DriverProfile driverProfile = driverProfileRepository.findByIdWithLinkedUserAccount(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ tài xế."));

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

        if (driverProfileRepository.existsByDriverCodeAndIdNot(driverCode, driverId)) {
            throw new IllegalArgumentException("Mã tài xế đã tồn tại.");
        }

        if (driverProfileRepository.existsByLicenseNumberAndIdNot(licenseNumber, driverId)) {
            throw new IllegalArgumentException("Số GPLX đã tồn tại.");
        }

        UserAccount linkedUserAccount = null;
        if (request.getLinkedUserAccountId() != null) {
            linkedUserAccount = userAccountRepository.findById(request.getLinkedUserAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Tài khoản DRIVER được chọn không tồn tại."));

            if (linkedUserAccount.getRole() != RoleName.DRIVER) {
                throw new IllegalArgumentException("Chỉ được liên kết với tài khoản có role DRIVER.");
            }

            if (driverProfileRepository.existsByLinkedUserAccount_IdAndIdNot(linkedUserAccount.getId(), driverId)) {
                throw new IllegalArgumentException("Tài khoản DRIVER này đã được gắn với hồ sơ tài xế khác.");
            }
        }

        driverProfile.setDriverCode(driverCode);
        driverProfile.setFullName(fullName);
        driverProfile.setPhoneNumber(phoneNumber);
        driverProfile.setLicenseNumber(licenseNumber);
        driverProfile.setLicenseExpiryDate(request.getLicenseExpiryDate());
        driverProfile.setAddress(address);
        driverProfile.setNotes(notes);
        driverProfile.setActive(request.isActive());
        driverProfile.setLinkedUserAccount(linkedUserAccount);

        driverProfileRepository.save(driverProfile);
    }

    @Transactional
    public boolean toggleDriverStatus(Long driverId) {
        DriverProfile driverProfile = driverProfileRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ tài xế."));

        driverProfile.setActive(!driverProfile.isActive());
        driverProfileRepository.save(driverProfile);
        return driverProfile.isActive();
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}