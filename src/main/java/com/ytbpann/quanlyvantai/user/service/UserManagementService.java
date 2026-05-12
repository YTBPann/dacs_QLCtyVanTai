package com.ytbpann.quanlyvantai.user.service;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import com.ytbpann.quanlyvantai.driver.repository.DriverProfileRepository;
import com.ytbpann.quanlyvantai.user.dto.UserCreateRequest;
import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.repository.UserAccountRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserManagementService {

    private final UserAccountRepository userAccountRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserAccountRepository userAccountRepository,
                                 DriverProfileRepository driverProfileRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserAccount> findAllUsers() {
        return userAccountRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public void createUser(UserCreateRequest request) {
        String username = safeTrim(request.getUsername());
        String fullName = safeTrim(request.getFullName());
        String password = request.getPassword();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username không được để trống");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Họ và tên không được để trống");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password không được để trống");
        }

        if (request.getRole() == null) {
            throw new IllegalArgumentException("Bạn phải chọn role");
        }

        if (userAccountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        String phoneNumber = safeTrim(request.getPhoneNumber());
        String address = safeTrim(request.getAddress());
        String driverCode = safeTrim(request.getDriverCode());
        String licenseNumber = safeTrim(request.getLicenseNumber());
        String notes = safeTrim(request.getNotes());

        if (request.getRole() == RoleName.DRIVER) {
            if (driverCode == null || driverCode.isBlank()) {
                throw new IllegalArgumentException("Mã tài xế không được để trống");
            }

            if (licenseNumber == null || licenseNumber.isBlank()) {
                throw new IllegalArgumentException("Số GPLX không được để trống");
            }

            if (driverProfileRepository.existsByDriverCode(driverCode)) {
                throw new IllegalArgumentException("Mã tài xế đã tồn tại");
            }

            if (driverProfileRepository.existsByLicenseNumber(licenseNumber)) {
                throw new IllegalArgumentException("Số GPLX đã tồn tại");
            }
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setRole(request.getRole());
        user.setEnabled(true);

        userAccountRepository.save(user);

        if (request.getRole() == RoleName.DRIVER) {
            DriverProfile driverProfile = new DriverProfile();
            driverProfile.setDriverCode(driverCode);
            driverProfile.setFullName(fullName);
            driverProfile.setPhoneNumber(phoneNumber);
            driverProfile.setLicenseNumber(licenseNumber);
            driverProfile.setLicenseExpiryDate(request.getLicenseExpiryDate());
            driverProfile.setAddress(address);
            driverProfile.setNotes(notes);
            driverProfile.setActive(true);
            driverProfile.setLinkedUserAccount(user);

            driverProfileRepository.save(driverProfile);
        }
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }

    public void changeUserStatus(Long userId, boolean enabled) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với id = " + userId));

        if (!enabled && user.getRole() == RoleName.ADMIN) {
            return;
        }

        user.setEnabled(enabled);
        userAccountRepository.save(user);
    }
}