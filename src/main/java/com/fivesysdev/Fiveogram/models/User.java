package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user")
    private List<Avatar> avatars;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Friendship> friendships;

    public void addAvatar(Avatar avatar) {
        avatars.add(avatar);
    }
}
