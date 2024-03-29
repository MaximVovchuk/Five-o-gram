package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import com.fivesysdev.Fiveogram.models.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class TextNotification extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @Column(name = "type")
    public NotificationType type;

    @Column(name = "entity_id")
    public Long entityId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public TextNotification(User owner, NotificationType type, Long entityId) {
        this.owner = owner;
        this.type = type;
        this.entityId = entityId;
    }
}
