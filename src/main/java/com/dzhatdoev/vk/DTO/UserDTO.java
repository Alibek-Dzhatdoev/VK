package com.dzhatdoev.vk.DTO;

import com.dzhatdoev.vk.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UserDTO {

    private static ModelMapper modelMapper = new ModelMapper();

    private long id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    private boolean canISubscribe = true;

    private List<PostDTO> postDTOList;

    public static UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }


    public static List<UserDTO> convertToDtoList(List<User> userList) {
        List<UserDTO> personDTOList = new ArrayList<>();
        for (User user : userList) personDTOList.add(convertToDto(user));
        return personDTOList;
    }
}