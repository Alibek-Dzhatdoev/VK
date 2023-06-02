package com.dzhatdoev.vk.repo;

import com.dzhatdoev.vk.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String name);

    Page<User> findAllByOrderByUsername(Pageable pageable);

    List<User>  findAllByOrderByUsername();

    Optional<User> findByEmail(String email);
}
