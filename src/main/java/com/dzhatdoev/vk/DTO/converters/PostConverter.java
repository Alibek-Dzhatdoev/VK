package com.dzhatdoev.vk.DTO.converters;

import com.dzhatdoev.vk.DTO.PostDTO;
import com.dzhatdoev.vk.DTO.PostDTOOut;
import com.dzhatdoev.vk.model.Post;
import com.dzhatdoev.vk.util.GoogleCloudStorage.CloudStorageService;
import com.dzhatdoev.vk.util.exceptions.InvalidImageException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostConverter {

    private static CloudStorageService cloudStorageService;
    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public PostConverter (CloudStorageService storageService) {
        cloudStorageService = storageService;
    }

    public static PostDTOOut convertToDto(Post post) {
        PostDTOOut dto = modelMapper.map(post, PostDTOOut.class);
        dto.setImage(cloudStorageService.downloadImage(post.getImageName()));
        return dto;
    }

    public static Post convertToPost(PostDTO postDTO, String generatedImageName) {
        Post post;
        try {
            post = modelMapper.map(postDTO, Post.class);
            post.setImageName(cloudStorageService.uploadImage(postDTO.getImage(), generatedImageName));
        } catch (IOException e) {
            throw new InvalidImageException("Invalid image");
        }
        return post;
    }

    public static Page<PostDTOOut> convertToDtoPage(Page<Post> postPage) {
        return postPage.map(PostConverter::convertToDto);
    }
}
