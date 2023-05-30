package com.dzhatdoev.vk.service;

import com.dzhatdoev.vk.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(User user) {
        userService.checkDbIsExistsByNameThrown(user.getUsername());
        userService.checkDbIsExistsByEmailThrown(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
    }
}
