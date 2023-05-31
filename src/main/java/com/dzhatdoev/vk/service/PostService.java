package com.dzhatdoev.vk.service;

import com.dzhatdoev.vk.DTO.PostDTO;
import com.dzhatdoev.vk.model.Post;
import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.repo.PostRepository;
import com.dzhatdoev.vk.util.exceptions.PostNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, @Lazy UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Post findByIdOrThrown(long id) {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post with that id does not exist"));
    }

    public List<Post> getFeed() {
        User current = userService.getCurrentUser();
        List<User> friends = current.getFriends();
        friends.addAll(current.getSubscriptions());
        List<Post> feed = new ArrayList<>();
        for (User friend : friends) {
            feed.addAll(friend.getPosts());
        }
        feed.sort((x, y) -> y.getCreatedAt().compareTo(x.getCreatedAt()));
        return feed;
    }

    @Transactional
    public void save(PostDTO postDTO) {
        Post post = PostDTO.convertToPost(postDTO);
        post.setAuthor(userService.getCurrentUser());
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }

    public void delete(long id) {
        postRepository.deleteById(id);
    }


    public ResponseEntity<String> updateOrThrown(long id, PostDTO postToUpdate) {
        User author = findByIdOrThrown(id).getAuthor();
        User currentUser = userService.getCurrentUser();
        // Проверяем, является ли пользователь владельцем цитаты
        if (currentUser.getId() == author.getId()) {
            // Обновляем данные цитаты
            Post post = findByIdOrThrown(id);
            post.setTitle(postToUpdate.getTitle());
            post.setText(postToUpdate.getText());
            post.setImagePath(postToUpdate.getImagePath()); //должна быть картинка, которая -> в путь
            postRepository.save(post);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Post updated");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have no access to change this quote");
        }
    }
}
