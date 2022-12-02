package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"name", "surname", "password", "role", "subscriptions"})
    private User owner;
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"name", "surname", "password", "role", "subscriptions"})
    private User friend;

    public Subscription(User owner, User newFriend) {
        this.owner = owner;
        this.friend = newFriend;
    }

}
