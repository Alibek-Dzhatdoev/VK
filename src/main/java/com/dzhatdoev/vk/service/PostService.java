package com.dzhatdoev.vk.service;

import com.dzhatdoev.vk.DTO.PostDTO;
import com.dzhatdoev.vk.DTO.PostDTOOut;
import com.dzhatdoev.vk.DTO.converters.PostConverter;
import com.dzhatdoev.vk.model.Post;
import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.repo.PostRepository;
import com.dzhatdoev.vk.util.exceptions.PostNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public Page<PostDTOOut> getUserPosts (long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return PostConverter.convertToDtoPage(postRepository.findByAuthorIdOrderByCreatedAtDesc(userId, pageable));
    }

    public Page<PostDTOOut> getFeed(int page, int size) {
        Pageable pageble = PageRequest.of(page, size);
        User current = userService.getCurrentUser();
        List<User> friendsAndSubscribes = current.getFriends();
        friendsAndSubscribes.addAll(current.getSubscribers())
        ;
        Page<Post> postPage = postRepository.findByAuthorInOrderByCreatedAtDesc(friendsAndSubscribes, pageble);
        return PostConverter.convertToDtoPage(postPage);
    }

    @Transactional
    public void save(PostDTO postDTO) {
        User current = userService.getCurrentUser();
        LocalDateTime now = LocalDateTime.now();
        Post post = PostConverter.convertToPost(postDTO, current.getUsername() + now);
        post.setAuthor(current);
        post.setCreatedAt(now);
        postRepository.save(post);
    }

    @Transactional
    public void delete(long id) {
        postRepository.deleteById(id);
    }


    @Transactional
    public ResponseEntity<String> updateOrThrown(long postId, PostDTO postToUpdate) {
        Post post = findByIdOrThrown(postId);

        User currentUser = userService.getCurrentUser();
        User author = userService.findByIdOrThrown(post.getAuthor().getId());

        Post newPostData = PostConverter.convertToPost(postToUpdate, author.getUsername() + LocalDateTime.now());
        // Проверяем, является ли пользователь владельцем цитаты
        if (currentUser.getId() == author.getId()) {
            // Обновляем данные цитаты
            post.setTitle(postToUpdate.getTitle());
            post.setText(postToUpdate.getText());
            post.setImageName(newPostData.getImageName());
            postRepository.save(post);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Post updated");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have no access to change this quote");
        }
    }
}
