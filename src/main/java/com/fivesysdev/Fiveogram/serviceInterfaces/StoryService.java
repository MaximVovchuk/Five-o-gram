package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status444NotYourStoryException;
import com.fivesysdev.Fiveogram.exceptions.Status445StoryNotFoundException;
import com.fivesysdev.Fiveogram.models.Story;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryService {

    Story createNewStory(String username, MultipartFile multipartFile) throws Status441FileIsNullException;

    List<Story> getStoriesList(String username);

    List<Story> getMyStoriesArchive(String username);

    void deleteById(String username, Long id) throws Status444NotYourStoryException, Status445StoryNotFoundException;

    Story getStoryById(Long id) throws Status445StoryNotFoundException;

    Story reportStory(String text, Long id) throws Status445StoryNotFoundException;

    void banStory(Long id);
}