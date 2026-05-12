package com.ytbpann.quanlyvantai.user.controller;

import com.ytbpann.quanlyvantai.user.dto.UserChangePasswordRequest;
import com.ytbpann.quanlyvantai.user.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class UserAccountController {

    private final UserManagementService userManagementService;

    public UserAccountController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new UserChangePasswordRequest());
        }

        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("request") UserChangePasswordRequest request,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "user/change-password";
        }

        try {
            userManagementService.changeOwnPassword(userDetails.getUsername(), request);
        } catch (IllegalArgumentException exception) {
            bindingResult.reject("changePasswordError", exception.getMessage());
            return "user/change-password";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công");
        return "redirect:/dashboard";
    }
}