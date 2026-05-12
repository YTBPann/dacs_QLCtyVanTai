package com.ytbpann.quanlyvantai.user.repository;

import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    List<UserAccount> findByRoleOrderByUsernameAsc(RoleName role);

    List<UserAccount> findByRoleAndEnabledTrueOrderByFullNameAsc(RoleName role);
}