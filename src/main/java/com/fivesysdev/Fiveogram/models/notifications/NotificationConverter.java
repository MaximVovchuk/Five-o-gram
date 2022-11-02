package com.fivesysdev.Fiveogram.models.notifications;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class NotificationConverter implements AttributeConverter<NotificationType, Long> {

    @Override
    public Long convertToDatabaseColumn(NotificationType type) {
        if (type == null) {
            return null;
        }
        return type.getCode();
    }

    @Override
    public NotificationType convertToEntityAttribute(Long code) {
        if (code == null) {
            return null;
        }
        return Stream.of(NotificationType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}