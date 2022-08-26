package com.fivesysdev.Fiveogram.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public interface HasLikeNotificationRecipients {
    @JsonIgnore
    List<User> getLikeNotificationRecipients();
}
