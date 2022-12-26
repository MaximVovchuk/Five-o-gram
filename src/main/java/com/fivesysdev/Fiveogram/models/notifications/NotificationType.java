package com.fivesysdev.Fiveogram.models.notifications;

public enum NotificationType {
    LIKE(1L),
    COMMENT(2L),
    SUBSCRIPTION(3L),
    COMMENTLIKE(4L),
    MARK(5L);
    private final Long code;

    NotificationType(Long code) {
        this.code = code;
    }

    public Long getCode() {
        return code;
    }
}
