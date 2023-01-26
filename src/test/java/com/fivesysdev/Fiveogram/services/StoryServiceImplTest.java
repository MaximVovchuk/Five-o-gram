package com.fivesysdev.Fiveogram.services;


import com.fivesysdev.Fiveogram.exceptions.Status444NotYourStoryException;
import com.fivesysdev.Fiveogram.exceptions.Status445StoryNotFoundException;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.models.Subscription;
import com.fivesysdev.Fiveogram.models.User;
import com.fivesysdev.Fiveogram.repositories.StoryReportRepository;
import com.fivesysdev.Fiveogram.repositories.StoryRepository;
import com.fivesysdev.Fiveogram.serviceInterfaces.FileService;
import com.fivesysdev.Fiveogram.serviceInterfaces.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoryServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private FileService fileService;
    @Mock
    private StoryRepository storyRepository;
    @Mock
    private StoryReportRepository storyReportRepository;

    @InjectMocks
    private StoryServiceImpl storyService;

    @Mock
    private MultipartFile multipartFile;

    @Test
    public void testGetStoriesList() {
        User user = User.builder().username("testuser").build();
        User friend1 = User.builder().username("friend1").build();
        User friend2 = User.builder().username("friend2").build();
        Story story1 = Story.builder().author(friend1).createdAt(LocalDateTime.now().minusDays(2)).build();
        Story story2 = Story.builder().author(friend1).createdAt(LocalDateTime.now().minusHours(1)).build();
        Story story3 = Story.builder().author(friend2).createdAt(LocalDateTime.now().minusHours(1)).build();
        user.setId(1L);
        friend2.setId(2L);
        friend1.setId(3L);
        story1.setId(1L);
        story2.setId(2L);
        story3.setId(3L);
        user.setSubscriptions(new ArrayList<>());
        friend1.setStories(new ArrayList<>());
        friend2.setStories(new ArrayList<>());


        Subscription subscription1 = Subscription.builder().friend(friend1).build();
        Subscription subscription2 = Subscription.builder().friend(friend2).build();
        user.getSubscriptions().add(subscription1);
        user.getSubscriptions().add(subscription2);
        friend1.getStories().add(story1);
        friend1.getStories().add(story2);
        friend2.getStories().add(story3);

        when(userService.findUserByUsername("testuser")).thenReturn(user);

        List<Story> result = storyService.getStoriesList("testuser");

        assertEquals(result.size(), 2);
        assertTrue(result.contains(story2));
        assertTrue(result.contains(story3));
        assertFalse(result.contains(story1));
        assertTrue(story1.isExpired());
    }

    @Test
    public void testGetMyStoriesArchive() {
        User user = User.builder().username("testuser").build();
        user.setId(1L);
        Story story1 = Story.builder().author(user).createdAt(LocalDateTime.now().minusDays(2)).build();
        story1.setId(1L);
        Story story2 = Story.builder().author(user).createdAt(LocalDateTime.now().minusDays(1)).build();
        story2.setId(1L);
        List<Story> stories = Arrays.asList(story1, story2);
        when(userService.findUserByUsername("testuser")).thenReturn(user);
        when(storyRepository.findAllByAuthor(user)).thenReturn(stories);

        List<Story> result = storyService.getMyStoriesArchive("testuser");

        assertEquals(result, stories);
        verify(storyRepository).findAllByAuthor(user);
    }

    @Test
    public void testDeleteById() throws Exception {
        Story story = Story.builder().author(User.builder().username("testuser").build()).build();
        story.setId(1L);

        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));
        when(userService.findUserByUsername("testuser")).thenReturn(story.getAuthor());
        storyService.deleteById("testuser", 1L);

        verify(storyRepository).deleteById(1L);
    }

    @Test
    public void testDeleteById_NotYourStory() {
        Story story = Story.builder().author(User.builder().username("not_testuser").build()).build();
        story.setId(1L);

        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));

        assertThrows(Status444NotYourStoryException.class, () -> storyService.deleteById("testuser", 1L));
    }

    @Test
    public void testDeleteById_StoryNotFound() {
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Status445StoryNotFoundException.class, () -> storyService.deleteById("testuser", 1L));
    }

    @Test
    public void testGetStoryById() throws Exception {
        Story story = Story.builder().author(User.builder().username("testuser").build()).build();
        story.setId(1L);

        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));

        Story result = storyService.getStoryById(1L);

        assertEquals(result, story);
    }

    @Test
    public void testGetStoryById_StoryNotFound() {
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Status445StoryNotFoundException.class, () -> storyService.getStoryById(1L));
    }

    @Test
    public void testReportStory() throws Exception {
        Story story = Story.builder().author(User.builder().username("testuser").build()).build();
        story.setId(1L);
        when(storyRepository.findById(1L)).thenReturn(Optional.of(story));

        Story result = storyService.reportStory("This story is offensive", 1L);

        assertEquals(result, story);
        verify(storyReportRepository).save(any());
    }

    @Test
    public void testReportStory_StoryNotFound() {
        when(storyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Status445StoryNotFoundException.class, () -> storyService.reportStory("This story is offensive", 1L));
    }

    @Test
    public void testBanStory() {
        storyService.banStory(1L);

        verify(storyRepository).deleteById(1L);
    }
}