package com.fivesysdev.Fiveogram.controllers;

import com.fivesysdev.Fiveogram.config.JWTUtil;
import com.fivesysdev.Fiveogram.exceptions.Status441FileException;
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

    @PostMapping("/new")
    public ResponseEntity<Story> addNewStory(@ModelAttribute MultipartFile multipartFile,
                                         @RequestHeader(value = "Authorization") String token)
            throws Status441FileException {
        return new ResponseEntity<>
                (storyService.createNewStory(jwtUtil.validate(token), multipartFile), HttpStatus.OK);
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
}
