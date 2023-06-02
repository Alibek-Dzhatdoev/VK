package com.dzhatdoev.vk.controller;

import com.dzhatdoev.vk.DTO.PostDTO;
import com.dzhatdoev.vk.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //смотреть ленту (посты друзей и подписок) ++
    @GetMapping
    public ResponseEntity<?> getFeed(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "100") int size) {
        return ResponseEntity.ok(postService.getFeed(page-1, size));
    }

    //создавать посты ++
    @PostMapping
    public ResponseEntity<?> createPost(@RequestParam("image") MultipartFile image,
                                        @RequestParam("title") String title,
                                        @RequestParam("text") String text) {

        PostDTO postDto = new PostDTO();
        postDto.setTitle(title);
        postDto.setText(text);
        postDto.setImage(image);
        postService.save(postDto);
        return ResponseEntity.ok("Post created successfully");
    }

    //удалять посты ++
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable("id") long id) {
        postService.delete(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    //обновлять посты  ++
    @PatchMapping("/{id}/update")
    public ResponseEntity<?> updatePost(@PathVariable("id") long postId,
                                        @RequestBody @Valid PostDTO post) {
        return postService.updateOrThrown(postId, post);
    }
}
