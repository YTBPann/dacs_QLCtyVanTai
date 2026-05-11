package com.ytbpann.quanlyvantai.driver.repository;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    @Query("""
            select d
            from DriverProfile d
            left join fetch d.linkedUserAccount
            order by d.id desc
            """)
    List<DriverProfile> findAllWithLinkedUserAccountOrderByIdDesc();

    boolean existsByDriverCode(String driverCode);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByLinkedUserAccount_Id(Long userAccountId);

    Optional<DriverProfile> findByDriverCode(String driverCode);
}