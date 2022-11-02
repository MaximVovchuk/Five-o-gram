package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class TextNotification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;
    @Column(name = "content")
    public String content;

    @Column(name = "type")
    public NotificationType type;

    @Column(name = "entity_id")
    public long entityId;

    @Column(name = "second_entity_id")
    public long secondEntityId;

    public TextNotification(String content, User owner, NotificationType type,long entityId,long secondEntityId) {
        this.content = content;
        this.owner = owner;
        this.type = type;
        this.entityId = entityId;
        this.secondEntityId = secondEntityId;
    }
}
