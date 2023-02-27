
package com.fivesysdev.Fiveogram.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class MessageModel extends BaseEntity {
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"name","surname","password","role","subscriptions"})
    private User user;
    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom;

    // TODO: 27/2/23 use builder better?
    public MessageModel(String content, User user, ChatRoom chatRoom) {
        this.content = content;
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
