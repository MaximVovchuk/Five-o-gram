package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.models.Post;
import com.fivesysdev.Fiveogram.models.SponsoredPost;
import com.fivesysdev.Fiveogram.models.notifications.Notification;
import com.fivesysdev.Fiveogram.models.notifications.TextNotification;
import com.fivesysdev.Fiveogram.repositories.NotificationRepository;
import com.fivesysdev.Fiveogram.repositories.SponsoredPostRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.NotificationService;
import com.fivesysdev.Fiveogram.util.Context;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SponsoredPostRepository sponsoredPostRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository, SponsoredPostRepository sponsoredPostRepository) {
        this.notificationRepository = notificationRepository;
        this.sponsoredPostRepository = sponsoredPostRepository;
    }

    public void sendNotification(Notification notification){
        if(notification.getObject() instanceof Post post){
            SponsoredPost sponsoredPost = sponsoredPostRepository.findByPost(post);
            if(sponsoredPost!=null){
                notificationRepository.save(new TextNotification
                        (notification.sendNotification(),sponsoredPost.getSponsor()));
            }
        }
        notificationRepository.save(new TextNotification(notification.sendNotification(), notification.getReceiver()));
    }

    @Override
    public List<String> getAllNotifications() {
        List<TextNotification> textNotifications = notificationRepository.findTextNotificationsByOwner(Context.getUserFromContext());
        List<String> result = new ArrayList<>();
        for(TextNotification notification : textNotifications){
            result.add(notification.getContent());
        }
        return result;
    }
}
