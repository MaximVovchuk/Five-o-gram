package com.fivesysdev.Fiveogram.services;

import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.StoryRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class StoryServiceImpl implements StoryService {
    private final UserService userService;
    private final FileService fileService;
    private final StoryRepository storyRepository;

    public StoryServiceImpl(UserService userService, FileService fileService, StoryRepository storyRepository) {
        this.userService = userService;
        this.fileService = fileService;
        this.storyRepository = storyRepository;
    }

    @Override
    public Story createNewStory(String username, MultipartFile multipartFile) throws Status441FileIsNullException {
        User author = userService.findUserByUsername(username);
        return storyRepository.save(
                Story.builder().author(author)
                        .pictureUrl(fileService.saveFile(author, multipartFile))
                        .createdAt(LocalDateTime.now())
                        .build());
    }

    @Override
    @Transactional
    public List<Story> getStoriesList(String username) {
        User user = userService.findUserByUsername(username);
        List<Story> stories = new ArrayList<>();
        List<Story> resultStories = new ArrayList<>();
        user.getSubscriptions().stream().map(Subscription::getFriend)
                .forEach(friend -> stories.addAll(friend.getUnexpiredStories()));
        for (Story story : stories) {
            if (LocalDateTime.now().isAfter(story.getCreatedAt().plusDays(1))) {
                story.setExpired(true);
            } else {
                resultStories.add(story);
            }
        }
        return resultStories;
    }

    @Override
    public List<Story> getMyStoriesArchive(String username) {
        return storyRepository.findAllByAuthor(userService.findUserByUsername(username));
    }
}
