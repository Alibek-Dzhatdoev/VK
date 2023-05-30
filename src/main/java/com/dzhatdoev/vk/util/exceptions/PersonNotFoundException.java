package com.dzhatdoev.vk.util.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PersonNotFoundException extends UsernameNotFoundException {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
