package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import com.fivesysdev.Fiveogram.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class TextNotification extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "type")
    public NotificationType type;

    @Column(name = "entity_id")
    public long entityId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public TextNotification(User owner, NotificationType type, long entityId) {
        this.owner = owner;
        this.type = type;
        this.entityId = entityId;
    }
}
