package com.ytbpann.quanlyvantai.driver.repository;

import com.ytbpann.quanlyvantai.driver.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    boolean existsByDriverCode(String driverCode);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByLinkedUserAccount_Id(Long userAccountId);

    boolean existsByDriverCodeAndIdNot(String driverCode, Long id);

    boolean existsByLicenseNumberAndIdNot(String licenseNumber, Long id);

    boolean existsByLinkedUserAccount_IdAndIdNot(Long userAccountId, Long id);

    @Query("""
            select dp
            from DriverProfile dp
            left join fetch dp.linkedUserAccount
            order by dp.id desc
            """)
    List<DriverProfile> findAllWithLinkedUserAccountOrderByIdDesc();

    @Query("""
            select dp
            from DriverProfile dp
            left join fetch dp.linkedUserAccount
            where dp.id = :id
            """)
    Optional<DriverProfile> findByIdWithLinkedUserAccount(@Param("id") Long id);
}