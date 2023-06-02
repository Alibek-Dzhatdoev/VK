package com.dzhatdoev.vk.controller;

import com.dzhatdoev.vk.DTO.UserDTO;
import com.dzhatdoev.vk.DTO.UserDTOForOwner;
import com.dzhatdoev.vk.model.User;
import com.dzhatdoev.vk.service.PostService;
import com.dzhatdoev.vk.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PostService postService;

    //  Посмотреть список всех людей ++
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false, defaultValue = "1") int page,
                                         @RequestParam(required = false, defaultValue = "100") int size) {
        Page<UserDTO> users = userService.findAll(page-1, size);
        return ResponseEntity.ok(users);
    }

    //Посмотреть профиль человека  ++
    //Посмотреть посты человека  ++
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id,
                                         @RequestParam(required = false, defaultValue = "1") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        User user = userService.findByIdOrThrown(id);
        User currentUser = userService.getCurrentUser();
        UserDTO userToShow = UserDTO.convertToDto(user);
        // Возвращаем только id, имя пользователя и посты, скрывая остальную информацию
        //if there is false - there should not be "subscribe" button on the browser page
        if (currentUser.equals(user) || currentUser.getFriends().contains(user) || currentUser.getSubscriptions().contains(user))
            userToShow.setCanISubscribe(false);
        userToShow.setPostPage(postService.getUserPosts(user.getId(), page , size));
        return ResponseEntity.ok(userToShow);
    }

    //    Посмотреть свой профиль    ++
    @GetMapping("/my_page")
    public ResponseEntity<?> goToMyPage(@RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "100") int size) {
        User me = userService.getCurrentUser();
        UserDTOForOwner userDTOForOwner = UserDTOForOwner.convertToDto(me);
        userDTOForOwner.setPostPage(postService.getUserPosts(me.getId(), page, size));
        return ResponseEntity.ok(userDTOForOwner);
    }

    // Метод для обновления информации о себе ++
    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTOForOwner userToUpdate) {
        userService.update(userToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body("Your data updated");
    }

    // Метод для удаления своей страницы ++
    @DeleteMapping
    public ResponseEntity<?> deleteMyPage() {
        return userService.deleteMyPage();
    }

    //подать заявку в друзья (подписаться)   ++
    //принять заявку в друзья (подписаться в ответ)   ++
    @GetMapping("/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable long id) {
        return ResponseEntity.ok(userService.subscribe(id));
    }

    //удалить из друзей (отписаться)
    @DeleteMapping({"/{id}/delete"})
    public ResponseEntity<?> deleteFromFriends(@PathVariable long id) {
        userService.deleteFromFriends(id);
        return ResponseEntity.ok("Person deleted from friends");
    }

    //отклонить заявку (оставить в подписчиках)
    @GetMapping({"/{id}/decline"})
    public ResponseEntity<?> declineRequest(@PathVariable long id) {
        userService.declineFriendRequest(id);
        return ResponseEntity.ok().build();
    }


    //отправить сообщение другу ++
    //отправка сообщений не реализована (как и сказано в тз)
    @PostMapping("/{id}/correspondence")
    public ResponseEntity<?> sendMessage(@RequestBody String message,
                                         @PathVariable("id") long userId) {
        return ResponseEntity.ok().build();
    }

    //посмотреть список своих друзей ++
    @GetMapping("/my_friends")
    public ResponseEntity<?> getMyFriends() {
        return ResponseEntity.ok(userService.getFriends(userService.getCurrentUser().getId()));
    }

    //посмотреть чьих-то друзей ++
    @GetMapping("/{id}/friends")
    public ResponseEntity<?> getFriends(@PathVariable long id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    //посмотреть список своих подписок  ++
    @GetMapping("/my_subscriptions")
    public ResponseEntity<?> getMySubscriptions() {
        return ResponseEntity.ok(userService.getSubscriptions(userService.getCurrentUser().getId()));
    }

    //посмотреть список чьих-то подписок  ++
    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<?> getSubscriptions(@PathVariable long id) {
        return ResponseEntity.ok(userService.getSubscriptions(id));
    }

    //посмотреть список своих подписчиков  ++
    @GetMapping("/my_subscribers")
    public ResponseEntity<?> getSubscribers() {
        return ResponseEntity.ok(userService.getSubscribers(userService.getCurrentUser().getId()));
    }

    //посмотреть список чьих-то подписчиков  ++
    @GetMapping("/{id}/subscribers")
    public ResponseEntity<?> getSubscribers(@PathVariable long id) {
        return ResponseEntity.ok(userService.getSubscribers(id));
    }
}
