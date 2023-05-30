package com.dzhatdoev.vk.service;

import com.dzhatdoev.vk.DTO.UserDTO;
import com.dzhatdoev.vk.DTO.UserDTOForOwner;
import com.dzhatdoev.vk.model.Post;
import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.repo.UserRepository;
import com.dzhatdoev.vk.util.exceptions.PersonNotCreatedException;
import com.dzhatdoev.vk.util.exceptions.PersonNotFoundException;
import com.dzhatdoev.vk.util.exceptions.PersonNotLoggedException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PostService postService;


    public List<User> findAll() {
        Hibernate.initialize(userRepository.findAll());
        return userRepository.findAll();
    }

    @Transactional()
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional()
    public void update(UserDTOForOwner personToUpdate) {
        User current = getCurrentUser();
        current.setUsername(personToUpdate.getUsername());
        current.setEmail(personToUpdate.getEmail());
        userRepository.save(current);
        setNewUsernameToCurrentUser(personToUpdate.getUsername());
    }

    public void setNewUsernameToCurrentUser(String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserDetails updatedUserDetails = new org.springframework.security.core.userdetails.User(newUsername, userDetails.getPassword(), userDetails.getAuthorities());
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }

    @Transactional
    public ResponseEntity<?> deleteMyPage() {
        userRepository.deleteById(getCurrentUser().getId());
        return ResponseEntity.ok("Your page has been deleted");
    }

    public User findByUsernameOrThrown(String name) {
        Optional<User> foundPerson = userRepository.findByUsername(name);
        return foundPerson.orElseThrow(() -> new PersonNotFoundException("Person with that name not found"));
    }

    public void checkDbIsExistsByNameThrown(String username) {
        Optional<User> person = userRepository.findByUsername(username);
        if (person.isPresent()) throw new PersonNotCreatedException("User with that username already exists");
    }

    public void checkDbIsExistsByEmailThrown(String email) {
        Optional<User> person = userRepository.findByEmail(email);
        if (person.isPresent()) throw new PersonNotCreatedException("User with that email already exists");
    }

    public User findByIdOrThrown(long id) {
        Optional<User> foundPerson = userRepository.findById(id);
        return foundPerson.orElseThrow(() -> new PersonNotFoundException("Person with that ID not found"));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            // обработка ошибки
            throw new PersonNotLoggedException("Please Sign In");
        } else {
            UserDetails userDetails = (UserDetails) principal;
            return findByUsernameOrThrown(userDetails.getUsername());
        }
    }



    public List<UserDTO> getFriends(long id) {
        User user = findByIdOrThrown(id);
        List<User> friends = user.getFriends();
        System.out.println(friends);
        System.out.println(user.getFriendsWith());
        friends.addAll(user.getFriendsWith());
        return UserDTO.convertToDtoList(friends);
    }

    public List<UserDTO> getSubscribers(long id) {
        User current = getCurrentUser();
        if (current.getId() == id) return UserDTO.convertToDtoList(current.getSubscribers());
        return UserDTO.convertToDtoList(findByIdOrThrown(id).getSubscribers());
    }

    public List<UserDTO> getSubscriptions(long id) {
        User current = getCurrentUser();
        Hibernate.initialize(current.getSubscriptions());
        if (current.getId() == id) return UserDTO.convertToDtoList(current.getSubscriptions());
        return UserDTO.convertToDtoList(findByIdOrThrown(id).getSubscriptions());
    }

    @Transactional
    public String subscribe(long userId) {
        //В методе findByIdOrThrown() мы уже проверили, можем ли мы подписаться на этого юзера.
        //Если флаг false, то кнопки "Подписаться"/"Добавить в друзья" не будет на страничке
        User currentUser = getCurrentUser();
        User targetUser = findByIdOrThrown(userId);
        //если нет в друзьях, но есть в подписчиках
        if (!currentUser.getFriends().contains(targetUser) &&
                currentUser.getSubscribers().contains(targetUser)) {
            currentUser.getFriends().add(targetUser);
            targetUser.getSubscriptions().remove(currentUser);
            return "Friend added!";
        } else if (currentUser.getFriends().contains(targetUser))
        {//Если есть в друзьях, то не будет кнопки "Добавить в друзья"
            return "User already is your friend";
        } else
        {
            //если нет в друзьях и нет в подписчиках
            currentUser.getSubscriptions().add(targetUser);
            return "You have sent a friend request.";
        }
    }


    @Transactional
    public void declineFriendRequest(long id) {
        User currentUser = getCurrentUser();
        User targetUser = findByIdOrThrown(id);
        if (!getCurrentUser().getSubscribers().contains(findByIdOrThrown(id))) {
            currentUser.getSubscribers().add(targetUser);
        }
    }

    @Transactional
    public void deleteFromFriends(long id) {
        User currentUser = getCurrentUser();
        User targetUser = findByIdOrThrown(id);
        List<User> friends = currentUser.getFriends();
        List<User> friendsWith = currentUser.getFriendsWith();
        if (friends.contains(targetUser) || friendsWith.contains(targetUser)) {
            friends.remove(targetUser);
            targetUser.getFriends().remove(currentUser);
        }
        targetUser.getSubscriptions().add(currentUser);
    }
}
