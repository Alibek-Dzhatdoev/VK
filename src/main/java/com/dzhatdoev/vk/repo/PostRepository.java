package com.dzhatdoev.vk.repo;

import com.dzhatdoev.vk.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(long id);
    List<Post> findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase (String st1, String st2);
    void deleteById(long id);
}
