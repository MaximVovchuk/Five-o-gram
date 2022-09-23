package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonIgnore
    private User owner;
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private User friend;

    public Friendship(User owner, User newFriend) {
        this.owner = owner;
        this.friend = newFriend;
    }

}
