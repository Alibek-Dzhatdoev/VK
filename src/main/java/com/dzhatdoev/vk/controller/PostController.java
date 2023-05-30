package com.dzhatdoev.vk.controller;

import com.dzhatdoev.vk.DTO.PostDTO;
import com.dzhatdoev.vk.service.PostService;
import com.dzhatdoev.vk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    //смотреть ленту (посты друзей и подписок) ++
    @GetMapping
    public ResponseEntity<List<PostDTO>> getFeed() {
        return ResponseEntity.ok(PostDTO.convertToDtoList(postService.getFeed()));
    }

    //создавать посты ++
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostDTO postDTO) {
        postService.save(postDTO);
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
    public ResponseEntity<?> updatePost(@PathVariable("id") long id,
                                        @RequestBody @Valid PostDTO postDTO) {
        return postService.updateOrThrown(id, postDTO);
    }
}
