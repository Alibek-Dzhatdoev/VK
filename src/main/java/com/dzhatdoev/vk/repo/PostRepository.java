package com.dzhatdoev.vk.repo;

import com.dzhatdoev.vk.model.Post;
import com.dzhatdoev.vk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(long id);

    Page<Post> findByAuthorInOrderByCreatedAtDesc (List<User> authorList, Pageable pageable);
    Page<Post> findByAuthorIdOrderByCreatedAtDesc (long authorId, Pageable pageable);

    //поиск по совпадениям слов
    List<Post> findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(String st1, String st2);

    void deleteById(long id);
}
