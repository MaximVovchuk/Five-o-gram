package com.fivesysdev.Fiveogram.serviceInterfaces;

import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
import com.fivesysdev.Fiveogram.models.Story;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoryService {

    Story createNewStory(String username, MultipartFile multipartFile) throws Status441FileException;

    List<Story> getStoriesList(String username);

    List<Story> getMyStoriesArchive(String username);
}