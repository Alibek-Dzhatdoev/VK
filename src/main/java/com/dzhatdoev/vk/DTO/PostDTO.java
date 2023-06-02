package com.dzhatdoev.vk.DTO;

import com.dzhatdoev.vk.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostDTO {

    private long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @NotNull
    private MultipartFile image;

    private User author;
}
