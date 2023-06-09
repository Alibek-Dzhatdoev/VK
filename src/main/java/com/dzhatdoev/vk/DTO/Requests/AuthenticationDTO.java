package com.dzhatdoev.vk.DTO.Requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class AuthenticationDTO {
    private static ModelMapper modelMapper = new ModelMapper();

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotEmpty
    private String password;
}
