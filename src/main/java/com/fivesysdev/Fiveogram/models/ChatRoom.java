package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.websocket.Session;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "chat_rooms")
public class ChatRoom extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"name", "surname", "password", "role", "subscriptions"})
    private User admin;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_room_users",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnoreProperties({"name", "surname", "password", "role", "subscriptions"})
    private Set<User> users;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    @ToString.Exclude
    private List<MessageModel> messages;
    @Transient
    private Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    public ChatRoom(User admin,Set<User> users) {
        this.admin = admin;
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
}
