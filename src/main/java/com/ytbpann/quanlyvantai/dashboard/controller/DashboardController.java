package com.ytbpann.quanlyvantai.dashboard.controller;

import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserAccountRepository userAccountRepository;

    public DashboardController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();

        UserAccount user = userAccountRepository.findByUsername(username).orElse(null);

        model.addAttribute("username", username);
        model.addAttribute("fullName", user != null ? user.getFullName() : username);
        model.addAttribute("role", user != null ? user.getRole().name() : "UNKNOWN");

        return "dashboard/index";
    }
}