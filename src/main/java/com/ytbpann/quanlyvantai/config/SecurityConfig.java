package com.ytbpann.quanlyvantai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // Quản lý tài khoản người dùng: chỉ ADMIN
                        .requestMatchers("/users", "/users/**").hasRole("ADMIN")

                        // Các module nghiệp vụ vận tải: ADMIN và MANAGER đều được vào
                        .requestMatchers("/admin/drivers", "/admin/drivers/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/vehicles", "/admin/vehicles/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/trips", "/admin/trips/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/locations", "/admin/locations/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/gps", "/admin/gps/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/gps/latest/vehicle-markers").hasAnyRole("ADMIN", "MANAGER")

                        // Các trang admin khác chưa khai báo rõ thì tạm vẫn chỉ ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Trang tài xế: chỉ DRIVER
                        .requestMatchers("/driver/**").hasRole("DRIVER")

                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}