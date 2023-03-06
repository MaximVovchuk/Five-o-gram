package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
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
    @JsonIncludeProperties({"id","username","avatars","stories"})
    private User owner;
    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    @JsonIncludeProperties({"id","username","avatars","stories"})
    private User friend;

    public Subscription(User owner, User newFriend) {
        this.owner = owner;
        this.friend = newFriend;
    }

}
