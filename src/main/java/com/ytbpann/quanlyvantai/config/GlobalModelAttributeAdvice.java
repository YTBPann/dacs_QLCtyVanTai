package com.ytbpann.quanlyvantai.config;

import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    private final UserAccountRepository userAccountRepository;

    public GlobalModelAttributeAdvice(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @ModelAttribute
    public void addCurrentUserInfo(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        String username = authentication.getName();

        if (username == null || username.isBlank() || "anonymousUser".equals(username)) {
            return;
        }

        UserAccount user = userAccountRepository.findByUsername(username).orElse(null);

        String fullName = username;
        String role = "UNKNOWN";

        if (user != null) {
            if (user.getFullName() != null && !user.getFullName().isBlank()) {
                fullName = user.getFullName();
            }

            if (user.getRole() != null) {
                role = user.getRole().name();
            }
        }

        model.addAttribute("username", username);
        model.addAttribute("fullName", fullName);
        model.addAttribute("role", role);
    }
}