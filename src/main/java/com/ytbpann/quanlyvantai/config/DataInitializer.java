package com.ytbpann.quanlyvantai.config;

import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userAccountRepository.existsByUsername("admin")) {
            UserAccount admin = new UserAccount(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "System Administrator",
                    RoleName.ADMIN,
                    true
            );
            userAccountRepository.save(admin);
            System.out.println(">>> Default admin account created: admin / admin123");
        }
    }
}