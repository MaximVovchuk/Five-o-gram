package com.fivesysdev.Fiveogram.models.notifications;

import com.fivesysdev.Fiveogram.models.BaseEntity;
import com.fivesysdev.Fiveogram.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
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


    public TextNotification(User owner, NotificationType type,long entityId) {
        this.owner = owner;
        this.type = type;
        this.entityId = entityId;
    }
}
