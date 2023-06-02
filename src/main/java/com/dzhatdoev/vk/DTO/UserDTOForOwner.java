package com.dzhatdoev.vk.DTO;

import com.dzhatdoev.vk.model.Post;
import com.dzhatdoev.vk.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@Getter
@Setter
@ToString
public class UserDTOForOwner {
    private static ModelMapper modelMapper = new ModelMapper();
    private int id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Email(message = "Must be in email format")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    private Page<PostDTOOut> postPage;

    public static UserDTOForOwner convertToDto(User user) {
        return modelMapper.map(user, UserDTOForOwner.class);
    }
}