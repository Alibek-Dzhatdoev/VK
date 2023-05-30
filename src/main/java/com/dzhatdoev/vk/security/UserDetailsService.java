package com.dzhatdoev.vk.security;

import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.service.UserService;
import com.dzhatdoev.vk.util.exceptions.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws PersonNotFoundException {
        User person = userService.findByUsernameOrThrown(username);
        return new org.springframework.security.core.userdetails.User(
                person.getUsername(),
                person.getPassword(),
//                Collections.singletonList(new SimpleGrantedAuthority(person.getRole()))
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
