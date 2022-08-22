package com.fivesysdev.Fiveogram.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@ToString(exclude = "friendships")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    @Size(min = 2, max = 32, message = "name must be between 2 and 32 characters long")
    private String name;
    @Column(name = "surname")
    @Size(min = 2, max = 32, message = "surname must be between 2 and 32 characters long")
    private String surname;
    @Column(name = "username")
    @Size(min = 6, max = 32, message = "username must be between 6 and 32 characters long")
    private String username;
    @Column(name = "password")
    @Length(min = 8, message = "password must be at least 8 characters long")
    private String password;
    @ManyToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Picture avatar;
    @OneToMany(mappedBy = "owner")
    private List<Friendship> friendships;

    public void addFriend(User friend) {
        friendships.add(new Friendship(this, friend));
    }
}
