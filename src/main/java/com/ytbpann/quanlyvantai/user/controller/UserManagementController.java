package com.ytbpann.quanlyvantai.user.controller;

import com.ytbpann.quanlyvantai.user.dto.UserCreateRequest;
import com.ytbpann.quanlyvantai.user.entity.RoleName;
import com.ytbpann.quanlyvantai.user.service.UserManagementService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        model.addAttribute("userCreateRequest", new UserCreateRequest());
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
            bindingResult.rejectValue("username", "duplicate", e.getMessage());
            return "user/create";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Tạo tài khoản thành công");
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