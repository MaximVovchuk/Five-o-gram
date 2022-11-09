package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.exceptions.Status441FileIsNullException;
import com.fivesysdev.Fiveogram.exceptions.Status444NotYourStoryException;
import com.fivesysdev.Fiveogram.exceptions.Status445StoryNotFoundException;
import com.fivesysdev.Fiveogram.models.Story;
import com.fivesysdev.Fiveogram.serviceInterfaces.StoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/story")
public class StoryController {
    private final StoryService storyService;
    private final JWTUtil jwtUtil;

    public StoryController(StoryService storyService, JWTUtil jwtUtil) {
        this.storyService = storyService;
        this.jwtUtil = jwtUtil;
    }
    //TODO story report
    @PostMapping("/new")
    public ResponseEntity<Story> addNewStory(@ModelAttribute MultipartFile multipartFile,
                                             @RequestHeader(value = "Authorization") String token)
            throws Status441FileIsNullException {
        return new ResponseEntity<>
                (storyService.createNewStory(jwtUtil.validate(token), multipartFile), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void deleteStory(@PathVariable Long id,
                            @RequestHeader(value = "Authorization") String token)
            throws Status444NotYourStoryException, Status445StoryNotFoundException {
        storyService.deleteById(jwtUtil.validateTokenAndRetrieveUsername(token), id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Story> getStory(@PathVariable Long id) throws Status445StoryNotFoundException {
        return new ResponseEntity<>(storyService.getStoryById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Story>> getStories(@RequestHeader(value = "Authorization") String token) {
        return new ResponseEntity<>
                (storyService.getStoriesList(jwtUtil.validate(token)), HttpStatus.OK);
    }

    @GetMapping("/archive")
    public ResponseEntity<List<Story>> getMyArchive(@RequestHeader(value = "Authorization") String token) {
        return new ResponseEntity<>
                (storyService.getMyStoriesArchive(jwtUtil.validate(token)), HttpStatus.OK);
    }

    @PostMapping("/{id:\\d+}/report")
    public ResponseEntity<Story> report(@PathVariable long id, @RequestBody String text) throws Status445StoryNotFoundException {
        return new ResponseEntity<>(
                storyService.reportStory(text,id),
                HttpStatus.OK);
    }
}
