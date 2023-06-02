package com.dzhatdoev.vk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Login must not be empty")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    @Column(unique = true)
    private String username;

    @Email(message = "Must be in email format")
    @NotEmpty(message = "Email must not be empty")
    @Column(unique = true)
    private String email;

    @Column
    @NotEmpty
    private String password;

    @OneToMany(mappedBy = "author")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private List<User> friends;

    @ManyToMany(mappedBy = "friends")
    @JsonIgnore
    private List<User> friendsWith;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "subscriptions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "target_user_id"))
    private List<User> subscriptions;

    @ManyToMany(mappedBy = "subscriptions")
    @JsonIgnore
    private List<User> subscribers;


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User person = (User) o;
        return Objects.equals(email, person.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
