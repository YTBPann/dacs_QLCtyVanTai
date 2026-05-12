package com.ytbpann.quanlyvantai.user.controller;

import com.ytbpann.quanlyvantai.user.dto.UserCreateRequest;
import com.ytbpann.quanlyvantai.user.dto.UserResetPasswordRequest;
import com.ytbpann.quanlyvantai.user.dto.UserUpdateRequest;
import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.entity.UserAccount;
import com.ytbpann.quanlyvantai.user.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserManagementController {

    private final UserManagementService userManagementService;

    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userManagementService.findAllUsers());
        return "user/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("userCreateRequest")) {
            model.addAttribute("userCreateRequest", new UserCreateRequest());
        }

        model.addAttribute("roles", List.of(RoleName.MANAGER, RoleName.DRIVER));
        return "user/create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("userCreateRequest") UserCreateRequest request,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        model.addAttribute("roles", List.of(RoleName.MANAGER, RoleName.DRIVER));

        if (bindingResult.hasErrors()) {
            return "user/create";
        }

        try {
            userManagementService.createUser(request);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();

            if ("Username đã tồn tại".equals(message)) {
                bindingResult.rejectValue("username", "duplicate", message);
            } else if ("Họ và tên không được để trống".equals(message)) {
                bindingResult.rejectValue("fullName", "required", message);
            } else if ("Password không được để trống".equals(message)) {
                bindingResult.rejectValue("password", "required", message);
            } else if ("Bạn phải chọn role".equals(message)) {
                bindingResult.rejectValue("role", "required", message);
            } else if ("Mã tài xế không được để trống".equals(message)) {
                bindingResult.rejectValue("driverCode", "required", message);
            } else if ("Số GPLX không được để trống".equals(message)) {
                bindingResult.rejectValue("licenseNumber", "required", message);
            } else if ("Mã tài xế đã tồn tại".equals(message)) {
                bindingResult.rejectValue("driverCode", "duplicate", message);
            } else if ("Số GPLX đã tồn tại".equals(message)) {
                bindingResult.rejectValue("licenseNumber", "duplicate", message);
            } else {
                model.addAttribute("errorMessage", message);
            }

            return "user/create";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Tạo tài khoản thành công");
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            UserAccount user = userManagementService.getUserById(id);

            if (!model.containsAttribute("userUpdateRequest")) {
                model.addAttribute("userUpdateRequest", userManagementService.getUpdateRequest(id));
            }

            model.addAttribute("user", user);
            model.addAttribute("userId", id);

            return "user/edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }
    }

    @Transactional(readOnly = true)
    public UserAccount getUserById(Long id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với ID: " + id));
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userUpdateRequest") UserUpdateRequest request,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        UserAccount user;

        try {
            user = userManagementService.getUserById(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }

        model.addAttribute("userId", id);
        model.addAttribute("user", user);

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        try {
            userManagementService.updateUser(id, request);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();

            if ("Username đã tồn tại".equals(message)) {
                bindingResult.rejectValue("username", "duplicate", message);
            } else if ("Username không được để trống".equals(message)) {
                bindingResult.rejectValue("username", "required", message);
            } else if ("Họ và tên không được để trống".equals(message)) {
                bindingResult.rejectValue("fullName", "required", message);
            } else {
                model.addAttribute("errorMessage", message);
            }

            model.addAttribute("user", userManagementService.getUserById(id));
            return "user/edit";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công");
        return "redirect:/users";
    }

    @GetMapping("/{id}/reset-password")
    public String showResetPasswordForm(@PathVariable Long id,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        UserAccount user;

        try {
            user = userManagementService.getUserById(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }

        if (user.getRole() == RoleName.ADMIN) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không được reset mật khẩu tài khoản ADMIN ở phase này");
            return "redirect:/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("resetPasswordRequest", new UserResetPasswordRequest());

        return "user/reset-password";
    }

    @PostMapping("/{id}/reset-password")
    public String resetPassword(@PathVariable Long id,
                                @Valid @ModelAttribute("resetPasswordRequest") UserResetPasswordRequest request,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        UserAccount user;

        try {
            user = userManagementService.getUserById(id);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/users";
        }

        if (user.getRole() == RoleName.ADMIN) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không được reset mật khẩu tài khoản ADMIN ở phase này");
            return "redirect:/users";
        }

        if (!bindingResult.hasFieldErrors("newPassword")
                && !bindingResult.hasFieldErrors("confirmPassword")
                && !Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Mật khẩu nhập lại không khớp");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user/reset-password";
        }

        userManagementService.resetPassword(id, request.getNewPassword());

        redirectAttributes.addFlashAttribute("successMessage",
                "Đã reset mật khẩu cho user: " + user.getUsername());

        return "redirect:/users";
    }

    @PostMapping("/{id}/enable")
    public String enableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userManagementService.changeUserStatus(id, true);
        redirectAttributes.addFlashAttribute("successMessage", "Đã bật tài khoản");
        return "redirect:/users";
    }

    @PostMapping("/{id}/disable")
    public String disableUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userManagementService.changeUserStatus(id, false);
        redirectAttributes.addFlashAttribute("successMessage", "Đã tắt tài khoản");
        return "redirect:/users";
    }
}