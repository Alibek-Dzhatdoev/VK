package com.dzhatdoev.vk.controller;

import com.dzhatdoev.vk.DTO.Requests.RegistrationRequest;
import com.dzhatdoev.vk.DTO.UserDTO;
import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {

    private final RegistrationService registrationService;

    //Создание учетной записи юзера
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping(("/registration"))
    @ResponseBody
    public ResponseEntity<?> registration(Model model, @Valid RegistrationRequest request) {
        User user = request.convertToUser();
        registrationService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.convertToDto(user));
    }

    @PostMapping(("/registrationform"))
    public String registrationForm(@ModelAttribute @Valid RegistrationRequest request) {
        User user = request.convertToUser();
        registrationService.register(user);
        return "redirect:/users";
    }
}





















