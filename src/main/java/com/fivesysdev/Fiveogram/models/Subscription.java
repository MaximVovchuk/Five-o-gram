package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    // TODO: 27/2/23 ask which properties are used (better choose @JsonIncludeProps?)
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
