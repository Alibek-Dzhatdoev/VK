package com.dzhatdoev.vk.DTO;

import com.dzhatdoev.vk.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTOOut {
    private long id;

    private String title;

    private String text;

    private String image;

    private User author;

    private LocalDateTime createdAt;
}
