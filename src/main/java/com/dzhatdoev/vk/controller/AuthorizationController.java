package com.dzhatdoev.vk.controller;

import com.dzhatdoev.vk.DTO.Requests.RegistrationDTO;
import com.dzhatdoev.vk.DTO.UserDTO;
import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.security.JWTUtil;
import com.dzhatdoev.vk.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {
    private final JWTUtil jwtUtil;
    private final RegistrationService registrationService;

    //Создание учетной записи юзера
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping(("/registration"))
    @ResponseBody
    public ResponseEntity<?> registration(@RequestBody @Valid RegistrationDTO registrationDTO,
                                          BindingResult bindingResult) {
        User user = registrationDTO.convertToUser();
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        registrationService.register(user);
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }

    @PostMapping(("/registrationform"))
    public String registrationForm(@ModelAttribute @Valid RegistrationDTO registrationDTO) {
        User user = registrationDTO.convertToUser();
        registrationService.register(user);
        return "redirect:/users";
    }
}





















