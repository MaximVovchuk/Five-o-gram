package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fivesysdev.Fiveogram.roles.Role;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private Role role;
    @Column(name = "email")
    private String email;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Avatar> avatars;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private List<Subscription> subscriptions;
    @OneToMany(mappedBy = "author")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    @ToString.Exclude
    private List<Story> stories;

    public User() {
        subscriptions = new ArrayList<>();
        avatars = new ArrayList<>();
        stories = new ArrayList<>();
    }

    public void addAvatar(Avatar avatar) {
        avatars.add(avatar);
    }

    @JsonIgnore
    public List<Story> getUnexpiredStories() {
        List<Story> unexpiredStories = new ArrayList<>();
        for (Story story : stories) {
            if (!story.isExpired()) {
                unexpiredStories.add(story);
            }
        }
        return unexpiredStories;
    }

}
