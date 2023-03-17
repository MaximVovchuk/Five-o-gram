package com.fivesysdev.Fiveogram.models;

import lombok.Getter;

@Getter
public enum ReportStatus {
    ACCEPTED(1L),
    REJECTED(2L);
    public final Long id;

    ReportStatus(Long id) {
        this.id = id;
    }
}
