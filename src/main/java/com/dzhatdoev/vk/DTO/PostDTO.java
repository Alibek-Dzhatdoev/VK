package com.dzhatdoev.vk.DTO;

import com.dzhatdoev.vk.model.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PostDTO {

    private static final ModelMapper mapper = new ModelMapper();

    private int id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    @NotNull
    private String imagePath;

    private LocalDateTime createdAt;


    //нужно загружать картинку в дто, а не путь

    public static Post convertToPost(PostDTO postDTO) {
        return mapper.map(postDTO, Post.class);
    }


    public static PostDTO convertToDTO(Post post) {
        return mapper.map(post, PostDTO.class);
    }

    public static List<PostDTO> convertToDtoList(List<Post> postList) {
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : postList) postDTOList.add(convertToDTO(post));
        return postDTOList;
    }


}
